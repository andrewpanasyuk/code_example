package examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.accountingsystem.model.Client;
import ua.com.foxminded.accountingsystem.model.CloseType;
import ua.com.foxminded.accountingsystem.model.Contract;
import ua.com.foxminded.accountingsystem.model.DealStatus;
import ua.com.foxminded.accountingsystem.repository.ClientRepository;
import ua.com.foxminded.accountingsystem.repository.ContractRepository;
import ua.com.foxminded.accountingsystem.repository.DealRepository;
import ua.com.foxminded.accountingsystem.service.DealService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class DealServiceJPA implements DealService {

    private final DealRepository dealRepository;
    private final ClientRepository clientRepository;
    private final ContractRepository contractRepository;

    @Autowired
    public DealServiceJPA(DealRepository dealRepository, ClientRepository clientRepository,
                          ContractRepository contractRepository) {
        this.dealRepository = dealRepository;
        this.clientRepository = clientRepository;
        this.contractRepository = contractRepository;
    }

    @Override
    public Deal createDealByClientId(Long id) {
        Client client = clientRepository.findOne(id);
        Deal deal = new Deal();
        deal.setClient(client);
        deal.setStatus(DealStatus.NEW);
        client.getDeals().add(deal);
        deal.setOpenDate(LocalDate.now());
        return deal;
    }

    @Override
    public void delete(Deal deal) {
        dealRepository.delete(deal);
    }

    @Override
    public Deal save(Deal deal) {
        return dealRepository.save(deal);
    }

    @Override
    public Deal findOne(Long id) {
        return dealRepository.findOne(id);
    }

    @Override
    public List<Deal> findAll() {
        return dealRepository.findAll();
    }

    @Override
    public List<Deal> findDealsByStatus(DealStatus dealStatus) {
        return dealRepository.findDealsByStatus(dealStatus);
    }

    @Override
    public void close(Deal deal, DealStatus dealStatus) {
        if (dealStatus == null) {
            recoveryDealStatus(deal);
        } else {
            deal.setStatus(dealStatus);
            deal.setCloseDate(LocalDate.now());
        }
        dealRepository.save(deal);
    }

    private void recoveryDealStatus(Deal deal) {
        if (contractRepository.existsContractByDealId(deal.getId())) {
            deal.setStatus(DealStatus.FROZEN);
        } else {
            deal.setStatus(DealStatus.NEW);
        }
    }

    @Override
    @Transactional
    public void freeze(Long id) {
        Deal deal = dealRepository.findOne(id);
        deal.setStatus(DealStatus.FROZEN);
        Contract contract = contractRepository.findContractByDealIdAndCloseTypeIsNull(id);
        contract.setCloseType(CloseType.FROZEN);
        contract.setCloseDate(LocalDate.now());
        contract.setClosingDescription("freeze");
        contractRepository.save(contract);
        dealRepository.save(deal);
    }

}
