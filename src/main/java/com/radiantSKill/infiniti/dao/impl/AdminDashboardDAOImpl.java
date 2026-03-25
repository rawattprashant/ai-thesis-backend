package com.radiantSKill.infiniti.dao.impl;

import com.radiantSKill.infiniti.dao.AdminDashboardDAO;
import com.radiantSKill.infiniti.dto.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminDashboardDAOImpl implements AdminDashboardDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<StudentDashboardDTO> getStudents(
            DashboardFilterRequest filter,
            int page,
            int size
    ) {

        String sql = """
            SELECT
            u.id,
            CONCAT(u.first_name,' ',u.last_name),
            tr.school_name,
            tr.grade,
            tr.has_digital_prototype,
            tr.has_investor_interest
            FROM app_user u
            JOIN thesis_registration tr
            ON u.id = tr.student_id
            WHERE 1=1
        """;

        if(filter.getGrade()!=null)
            sql += " AND tr.grade = :grade";

        if(filter.getSection()!=null)
            sql += " AND tr.section = :section";

        if(filter.getDigitalPrototype()!=null)
            sql += " AND tr.has_digital_prototype = :prototype";

        if(filter.getInvestment()!=null)
            sql += " AND tr.has_investor_interest = :investment";

        Query query = entityManager.createNativeQuery(sql);

        if(filter.getGrade()!=null)
            query.setParameter("grade",filter.getGrade());

        if(filter.getSection()!=null)
            query.setParameter("section",filter.getSection());

        if(filter.getDigitalPrototype()!=null)
            query.setParameter("prototype",filter.getDigitalPrototype());

        if(filter.getInvestment()!=null)
            query.setParameter("investment",filter.getInvestment());

        query.setFirstResult(page*size);
        query.setMaxResults(size);

        List<Object[]> rows = query.getResultList();

        return rows.stream().map(r ->
                new StudentDashboardDTO(
                        ((Number) r[0]).longValue(),
                        (String) r[1],
                        (String) r[2],
                        (String) r[3],
                        (Boolean) r[4],
                        (Boolean) r[5]
                )
        ).toList();
    }
}