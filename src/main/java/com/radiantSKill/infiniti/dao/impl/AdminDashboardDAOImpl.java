package com.radiantSKill.infiniti.dao.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.radiantSKill.infiniti.dao.AdminDashboardDAO;
import com.radiantSKill.infiniti.dto.DashboardFilterRequest;
import com.radiantSKill.infiniti.dto.StudentDashboardDTO;
import com.radiantSKill.infiniti.entity.QAppUser;
import com.radiantSKill.infiniti.entity.QThesisRegistration;
import com.radiantSKill.infiniti.util.Constants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminDashboardDAOImpl implements AdminDashboardDAO {

    @PersistenceContext
    private EntityManager entityManager;

    QAppUser qAppUser = QAppUser.appUser;
    QThesisRegistration qThesisRegistration = QThesisRegistration.thesisRegistration;

    @Override
    public List<StudentDashboardDTO> getStudents(
            DashboardFilterRequest filter,
            int page,
            int size
    ) {


        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        BooleanBuilder builder = new BooleanBuilder();

        // ✅ Gender
        if (filter.getGender() != null && !filter.getGender().isBlank()) {
            builder.and(qThesisRegistration.gender.equalsIgnoreCase(filter.getGender()));
        }

        // ✅ Grade
        if (filter.getGrade() != null && !filter.getGrade().isBlank()) {
            builder.and(qThesisRegistration.grade.eq(filter.getGrade()));
        }

        // ✅ Section
        if (filter.getSection() != null && !filter.getSection().isBlank()) {
            builder.and(qThesisRegistration.section.eq(filter.getSection()));
        }

        // ✅ Digital Prototype
        if (filter.getDigitalPrototype() != null && !filter.getDigitalPrototype().isBlank()) {
            if (filter.getDigitalPrototype().equalsIgnoreCase(Constants.WITH_PROTOTYPE)) {
                builder.and(qThesisRegistration.hasDigitalPrototype.isTrue());
            } else if (filter.getDigitalPrototype().equalsIgnoreCase(Constants.WITHOUT_PROTOTYPE)) {
                builder.and(qThesisRegistration.hasDigitalPrototype.isFalse());
            }
        }

        // ✅ Investment
        if (filter.getInvestment() != null && !filter.getInvestment().isBlank()) {
            if (filter.getInvestment().equalsIgnoreCase(Constants.INVESTIBLE)) {
                builder.and(qThesisRegistration.hasInvestorInterest.isTrue());
            } else if (filter.getInvestment().equalsIgnoreCase(Constants.NON_INVESTIBLE)) {
                builder.and(qThesisRegistration.hasInvestorInterest.isFalse());
            }
        }

        return queryFactory
                .select(Projections.constructor(
                        StudentDashboardDTO.class,
                        qAppUser.id,
                        qAppUser.firstName.concat(" ").concat(qAppUser.lastName),
                        qThesisRegistration.schoolName,
                        qThesisRegistration.grade,
                        qThesisRegistration.hasDigitalPrototype,
                        qThesisRegistration.hasInvestorInterest
                ))
                .from(qAppUser)
                .join(qThesisRegistration).on(qAppUser.id.eq(qThesisRegistration.student.id))
                .where(builder)
                .offset(page * size)
                .limit(size)
                .fetch();
    }
}