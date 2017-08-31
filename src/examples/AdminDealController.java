package examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.accountingsystem.model.DealStatus;
import ua.com.foxminded.accountingsystem.service.ClientService;
import ua.com.foxminded.accountingsystem.service.ConsultancyService;
import ua.com.foxminded.accountingsystem.service.ContractService;
import ua.com.foxminded.accountingsystem.service.DealService;
import ua.com.foxminded.accountingsystem.service.InvoiceService;
import ua.com.foxminded.accountingsystem.service.SalaryItemService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/deals")
public class AdminDealController {

    private final DealService dealService;
    private final ClientService clientService;
    private final ConsultancyService consultancyService;
    private final ContractService contractService;
    private final SalaryItemService salaryItemService;
    private final InvoiceService invoiceService;

    @Autowired
    public AdminDealController(DealService dealService, ClientService clientService,
                               ConsultancyService consultancyService, ContractService contractService,
                               SalaryItemService salaryItemService, InvoiceService invoiceService) {
        this.dealService = dealService;
        this.clientService = clientService;
        this.consultancyService = consultancyService;
        this.contractService = contractService;
        this.salaryItemService = salaryItemService;
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public String create(@Valid Deal deal, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("consultancies", consultancyService.findAll());
            return "admin/deal";
        }
        dealService.save(deal);
        return "redirect:/admin/deals";
    }

    @GetMapping
    public String getAllDeals(Model model) {
        List<Deal> deals = dealService.findAll();
        model
            .addAttribute("title", "Deals")
            .addAttribute("deals", deals);
        return "admin/deals";
    }

    @GetMapping(value = "/{id}")
    public String getDealById(@PathVariable long id, Model model) {
        Deal deal = dealService.findOne(id);
        model.addAttribute("deal", deal)
            .addAttribute("title", "Deal: " + deal.getId())
            .addAttribute("consultancies", consultancyService.findAll())
            .addAttribute("contracts", contractService.findAllByDeal(deal));
        return "admin/deal";
    }

    @DeleteMapping(value = "/{id}")
    public String removeDeal(@PathVariable long id) {
        dealService.delete(dealService.findOne(id));
        return "redirect:/admin/deals";
    }

    @GetMapping(value = "/create")
    public String addDeal(@RequestParam long clientId, Model model) {
        Deal deal = dealService.createDealByClientId(clientId);
        model.addAttribute("deal", deal)
            .addAttribute("consultancies", consultancyService.findAll());
        return "admin/deal";
    }

    @GetMapping(value = "/new")
    public String placedDeals(Model model) {
        List<Deal> deals = dealService.findDealsByStatus(DealStatus.NEW);
        model.addAttribute("deals", deals);
        return "admin/deals";
    }

    @GetMapping(value = "/{id}/freeze")
    public String freezeDeal(@PathVariable long id) {
        salaryItemService.createPretermSalaryItem(invoiceService.findLastInvoiceInActiveContractByDealId(id), LocalDate.now());
        dealService.freeze(id);
        return "redirect:/admin/deals";
    }
}
