package com.radiantSKill.infiniti.dao.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.radiantSKill.infiniti.dao.PrincipalDashboardDAO;
import com.radiantSKill.infiniti.dto.PrincipalDashboardFilterRequest;
import com.radiantSKill.infiniti.dto.PrincipalStudentDTO;
import com.radiantSKill.infiniti.entity.QAppUser;
import com.radiantSKill.infiniti.entity.QRole;
import com.radiantSKill.infiniti.entity.QStudentSubmissionStore;
import com.radiantSKill.infiniti.entity.QThesisRegistration;
import com.radiantSKill.infiniti.util.Constants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PrincipalDashboardDAOImpl implements PrincipalDashboardDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    QRole qRole = QRole.role;
    QAppUser qAppUser = QAppUser.appUser;
    QThesisRegistration qThesisRegistration = QThesisRegistration.thesisRegistration;
    QStudentSubmissionStore qStore = QStudentSubmissionStore.studentSubmissionStore;
    @Override
    public List<PrincipalStudentDTO> getStudentsBySchool(
            String schoolName,
            int page,
            int size
    ) {

        return queryFactory
                .select(Projections.constructor(
                        PrincipalStudentDTO.class,
                        qAppUser.id,
                        qAppUser.firstName
                                .concat(" ")
                                .concat(qAppUser.lastName),
                        qAppUser.email,
                        qAppUser.status,
                        qStore.overallStatus
                ))
                .distinct()
                .from(qStore)
                .join(qStore.student, qAppUser)
                .join(qAppUser.roles, qRole)
                .join(qThesisRegistration)
                .on(qThesisRegistration.student.eq(qAppUser))
                .where(
                        qRole.name.eq("STUDENT")
                                .and(
                                        qThesisRegistration.schoolName
                                                .eq(schoolName)
                                )
                )
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }
    @Override
    public List<PrincipalStudentDTO> getStudents(
            String schoolName,
            PrincipalDashboardFilterRequest filter,
            int page,
            int size
    ) {

        BooleanBuilder builder = new BooleanBuilder();

        // School filter
        builder.and(
                qThesisRegistration.schoolName.eq(schoolName)
        );

        // Gender
        if (filter.getGender() != null &&
                !filter.getGender().isBlank()) {

            builder.and(
                    qThesisRegistration.gender
                            .equalsIgnoreCase(filter.getGender())
            );
        }

        // Grade
        if (filter.getGrade() != null &&
                !filter.getGrade().isBlank()) {

            builder.and(
                    qThesisRegistration.grade.eq(filter.getGrade())
            );
        }

        // Section
        if (filter.getSection() != null &&
                !filter.getSection().isBlank()) {

            builder.and(
                    qThesisRegistration.section.eq(filter.getSection())
            );
        }

        // Prototype
        if (filter.getDigitalPrototype() != null &&
                !filter.getDigitalPrototype().isBlank()) {

            if (filter.getDigitalPrototype()
                    .equalsIgnoreCase(Constants.WITH_PROTOTYPE)) {

                builder.and(
                        qThesisRegistration
                                .hasDigitalPrototype
                                .isTrue()
                );

            } else if (filter.getDigitalPrototype()
                    .equalsIgnoreCase(Constants.WITHOUT_PROTOTYPE)) {

                builder.and(
                        qThesisRegistration
                                .hasDigitalPrototype
                                .isFalse()
                );
            }
        }

        // Investment
        if (filter.getInvestment() != null &&
                !filter.getInvestment().isBlank()) {

            if (filter.getInvestment()
                    .equalsIgnoreCase(Constants.INVESTIBLE)) {

                builder.and(
                        qThesisRegistration
                                .hasInvestorInterest
                                .isTrue()
                );

            } else if (filter.getInvestment()
                    .equalsIgnoreCase(Constants.NON_INVESTIBLE)) {

                builder.and(
                        qThesisRegistration
                                .hasInvestorInterest
                                .isFalse()
                );
            }
        }

        return queryFactory
                .select(Projections.constructor(
                        PrincipalStudentDTO.class,
                        qAppUser.id,
                        qAppUser.firstName
                                .concat(" ")
                                .concat(qAppUser.lastName),
                        qAppUser.email,
                        qAppUser.status,
                        qStore.overallStatus
                ))
                .distinct()
                .from(qThesisRegistration)
                .join(qThesisRegistration.student, qAppUser)
                .join(qAppUser.roles, qRole)
                .join(qStore)
                .on(qStore.student.eq(qAppUser))
                .where(
                        qRole.name.eq("STUDENT")
                                .and(builder)
                )
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }
}
