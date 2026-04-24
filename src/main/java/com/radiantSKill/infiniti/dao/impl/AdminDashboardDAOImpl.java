package com.radiantSKill.infiniti.dao.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.radiantSKill.infiniti.dao.AdminDashboardDAO;
import com.radiantSKill.infiniti.dto.AdminStudentDTO;
import com.radiantSKill.infiniti.dto.DashboardFilterRequest;
import com.radiantSKill.infiniti.dto.StudentDashboardDTO;
import com.radiantSKill.infiniti.entity.*;
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

    private final JPAQueryFactory queryFactory;

    QRole qRole = QRole.role;
    QAppUser qAppUser = QAppUser.appUser;
    QThesisRegistration qThesisRegistration = QThesisRegistration.thesisRegistration;
    QStudentSubmissionStore qStore = QStudentSubmissionStore.studentSubmissionStore;

    @Override
    public List<AdminStudentDTO> getAllStudents(int page, int size) {

        return queryFactory
                .select(Projections.constructor(
                        AdminStudentDTO.class,
                        qAppUser.id,
                        qAppUser.firstName.concat(" ").concat(qAppUser.lastName),
                        qAppUser.email,
                        qAppUser.status,
                        qStore.overallStatus
                ))
                .distinct()
                .from(qStore)
                .join(qStore.student, qAppUser)
                .join(qAppUser.roles, qRole)
                .where(qRole.name.eq("STUDENT"))
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }

    @Override
    public List<AdminStudentDTO> getStudents(
            DashboardFilterRequest filter,
            int page,
            int size
    ) {

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

        // ✅ Digital Prototype (Yes / No)
        if (filter.getDigitalPrototype() != null && !filter.getDigitalPrototype().isBlank()) {
            if (filter.getDigitalPrototype().equalsIgnoreCase(Constants.WITH_PROTOTYPE)) {
                builder.and(qThesisRegistration.hasDigitalPrototype.isTrue());
            } else if (filter.getDigitalPrototype().equalsIgnoreCase(Constants.WITHOUT_PROTOTYPE)) {
                builder.and(qThesisRegistration.hasDigitalPrototype.isFalse());
            }
        }

        // ✅ Investment (Yes / No)
        if (filter.getInvestment() != null && !filter.getInvestment().isBlank()) {
            if (filter.getInvestment().equalsIgnoreCase(Constants.INVESTIBLE)) {
                builder.and(qThesisRegistration.hasInvestorInterest.isTrue());
            } else if (filter.getInvestment().equalsIgnoreCase(Constants.NON_INVESTIBLE)) {
                builder.and(qThesisRegistration.hasInvestorInterest.isFalse());
            }
        }

        return queryFactory
                .select(Projections.constructor(
                        AdminStudentDTO.class,
                        qAppUser.id,
                        qAppUser.firstName.concat(" ").concat(qAppUser.lastName),
                        qAppUser.email,
                        qAppUser.status,
                        qStore.overallStatus
                ))
                .distinct()
                .from(qThesisRegistration) // ✅ IMPORTANT: start from registration
                .join(qThesisRegistration.student, qAppUser)
                .join(qAppUser.roles, qRole)
                .join(qStore).on(qStore.student.eq(qAppUser))
                .where(
                        qRole.name.eq("STUDENT")
                                .and(builder)
                )
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }

}