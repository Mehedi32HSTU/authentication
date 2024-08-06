package com.reve.authentication.server.menu.permision;

import com.reve.authentication.server.common.RootRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MenuPermissionRepository extends RootRepository<MenuPermission, Long> {
    public List<MenuPermission> findAllByRoleIdAndIsDeleted(Long roleId, Boolean isDeleted);
    @Query(value = "SELECT role_id FROM menu_permission WHERE menu_id = :menuId AND is_deleted = :isDeleted", nativeQuery = true)
    Set<Long> findAllRoleIdByMenuIdAndIsDeleted(@Param("menuId") Long menuId, @Param("isDeleted") Boolean isDeleted);
    @Query(value = "SELECT menu_id FROM menu_permission WHERE role_id = :roleId AND is_deleted = :isDeleted", nativeQuery = true)
    Set<Long> findAllMenuIdByRoleIdAndIsDeleted(@Param("roleId") Long roleId, @Param("isDeleted") Boolean isDeleted);

    public void deleteAllByRoleId(Long roleId);
}
