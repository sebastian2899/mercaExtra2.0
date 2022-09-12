package com.mercaextra.app.specification;

import com.mercaextra.app.domain.Caja;
import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

public class CajaSpecification {

    public static Specification<Caja> cajaByValue(BigDecimal valueCaja) {
        return (root, query, cb) -> cb.equal(root.get("valorTotalDia"), valueCaja);
    }

    public static Specification<Caja> cajaByEstado(String estado) {
        return (root, query, cb) -> cb.equal(root.get("estado"), estado);
    }
}
