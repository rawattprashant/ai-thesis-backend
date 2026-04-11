package com.radiantSKill.infiniti.dao.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.radiantSKill.infiniti.dao.UserDAO;
import com.radiantSKill.infiniti.entity.QRole;
import com.radiantSKill.infiniti.entity.QUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {

    private final JPAQueryFactory queryFactory;

    @Override
    public String findRoleByUserId(Long userId) {

        QUserRole userRole = QUserRole.userRole;
        QRole role = QRole.role;

        return queryFactory
                .select(role.name)
                .from(userRole)
                .join(userRole.role, role)
                .where(userRole.user.id.eq(userId))
                .fetchFirst();
    }
}