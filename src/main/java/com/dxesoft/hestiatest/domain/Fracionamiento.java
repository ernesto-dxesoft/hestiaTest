package com.dxesoft.hestiatest.domain;

import com.dxesoft.hestiatest.domain.enumeration.StatusFraccionamiento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Fracionamiento entity.\n@author A true hipster
 */
@ApiModel(description = "The Fracionamiento entity.\n@author A true hipster")
@Entity
@Table(name = "fracionamiento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fracionamiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * name
     */
    @NotNull
    @Size(max = 200)
    @ApiModelProperty(value = "name", required = true)
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    /**
     * startDate
     */
    @NotNull
    @ApiModelProperty(value = "startDate", required = true)
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * totalHouses
     */
    @NotNull
    @Min(value = 49)
    @ApiModelProperty(value = "totalHouses", required = true)
    @Column(name = "total_houses", nullable = false)
    private Integer totalHouses;

    /**
     * costByHouse
     */
    @NotNull
    @ApiModelProperty(value = "costByHouse", required = true)
    @Column(name = "cost_by_house", precision = 21, scale = 2, nullable = false)
    private BigDecimal costByHouse;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusFraccionamiento status;

    @NotNull
    @Column(name = "contract", nullable = false)
    private String contract;

    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fracionamiento id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Fracionamiento name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Fracionamiento startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getTotalHouses() {
        return this.totalHouses;
    }

    public Fracionamiento totalHouses(Integer totalHouses) {
        this.totalHouses = totalHouses;
        return this;
    }

    public void setTotalHouses(Integer totalHouses) {
        this.totalHouses = totalHouses;
    }

    public BigDecimal getCostByHouse() {
        return this.costByHouse;
    }

    public Fracionamiento costByHouse(BigDecimal costByHouse) {
        this.costByHouse = costByHouse;
        return this;
    }

    public void setCostByHouse(BigDecimal costByHouse) {
        this.costByHouse = costByHouse;
    }

    public StatusFraccionamiento getStatus() {
        return this.status;
    }

    public Fracionamiento status(StatusFraccionamiento status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusFraccionamiento status) {
        this.status = status;
    }

    public String getContract() {
        return this.contract;
    }

    public Fracionamiento contract(String contract) {
        this.contract = contract;
        return this;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public Address getAddress() {
        return this.address;
    }

    public Fracionamiento address(Address address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fracionamiento)) {
            return false;
        }
        return id != null && id.equals(((Fracionamiento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fracionamiento{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", totalHouses=" + getTotalHouses() +
            ", costByHouse=" + getCostByHouse() +
            ", status='" + getStatus() + "'" +
            ", contract='" + getContract() + "'" +
            "}";
    }
}
