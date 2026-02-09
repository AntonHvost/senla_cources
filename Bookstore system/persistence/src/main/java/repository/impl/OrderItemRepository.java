package repository.impl;

import di.annotation.Component;
import domain.model.impl.OrderItem;
import org.hibernate.Criteria;
import repository.BaseRepository;
import util.HibernateUtil;

import javax.persistence.criteria.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderItemRepository extends BaseRepository<OrderItem, Long> {

   public OrderItemRepository() {
       super(OrderItem.class);
   }

    public List<OrderItem> getItemByOrderId(Long orderId) {

        CriteriaBuilder cb = HibernateUtil.getSession().getCriteriaBuilder();
        CriteriaQuery<OrderItem> cq = cb.createQuery(OrderItem.class);
        Root<OrderItem> root = cq.from(OrderItem.class);

        ParameterExpression<Long> p1 = cb.parameter(Long.class, "orderId");
        Predicate p = cb.equal(root.get("orderId"), p1);

        cq.select(root).where(p);

        List<OrderItem> res = HibernateUtil.getSession().createQuery(cq)
                .setParameter("orderId", orderId)
                .getResultList();

        return res;
    }
}
