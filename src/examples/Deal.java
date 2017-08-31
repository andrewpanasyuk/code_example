package examples;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "deal")
@Audited
public class Deal extends AbstractAuditEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deal_sequence")
    @SequenceGenerator(name = "deal_sequence", sequenceName = "deal_sequence", initialValue = 50)
    private Long id;

    @Column(name = "open_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate openDate;

    @Column(name = "close_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closeDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull(message = "You have to choose 'Consultancy'")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultancy_id")
    private Consultancy consultancy;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DealStatus status;

    public Consultancy getConsultancy() {
        return consultancy;
    }

    public void setConsultancy(Consultancy consultancy) {
        this.consultancy = consultancy;
    }

    public DealStatus getStatus() {
        return status;
    }

    public void setStatus(DealStatus status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    @Override
    public String toString() {
        return "Deal{" +
            "id=" + id +
            ", openDate=" + openDate +
            ", closeDate=" + closeDate +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deal deal = (Deal) o;
        return id.equals(deal.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return 31;
        }
        return id.hashCode();
    }

}
