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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "deal_queue")
@Audited
public class DealQueue extends AbstractAuditEntity implements Comparable<DealQueue> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deal_queue_sequence")
    @SequenceGenerator(name = "deal_queue_sequence", sequenceName = "deal_queue_sequence", initialValue = 50)
    private Long id;

    @Column(name = "queuing_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate queuingDate;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id")
    private Deal deal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getQueuingDate() {
        return queuingDate;
    }

    public void setQueuingDate(LocalDate queuingDate) {
        this.queuingDate = queuingDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DealQueue dealQueue = (DealQueue) o;
        return id.equals(dealQueue.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return 31;
        }
        return id.hashCode();
    }

    @Override
    public int compareTo(DealQueue dealQueue) {
        int value = getPriority().name().compareTo(dealQueue.getPriority().name());
        if (value != 0) {
            return value;
        }
        return getQueuingDate().compareTo(dealQueue.getQueuingDate());
    }
}
