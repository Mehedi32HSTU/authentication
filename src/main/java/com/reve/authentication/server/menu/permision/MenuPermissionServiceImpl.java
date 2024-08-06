package com.reve.authentication.server.menu.permision;

import com.reve.authentication.server.common.ExceptionResponseHandler;
import com.reve.authentication.server.menu.Menu;
import com.reve.authentication.server.menu.MenuRepository;
import com.reve.authentication.server.menu.MenuServiceHelper;
import com.reve.authentication.server.payload.response.MessageResponse;
import com.reve.authentication.server.role.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MenuPermissionServiceImpl implements MenuPermissionService, ExceptionResponseHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MenuPermissionRepository menuPermissionRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuServiceHelper menuServiceHelper;
    @Override
    public ResponseEntity<?> addMenuPermission(MenuPermission menuPermission) {
        try {
            logger.info("addMenuPermission Service Called.");
            if(menuRepository.existsById(menuPermission.getMenuId()) && roleRepository.existsById(menuPermission.getRoleId())) {
                menuPermission.setIsDeleted(false);
                return ResponseEntity.status(HttpStatus.OK).body(menuPermissionRepository.saveAndFlush(menuPermission));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Menu or Role Not Found"));
        } catch (Exception e) {
            return handleException(e, "addMenuPermission");
        }
    }

    @Override
    public ResponseEntity<?> getMenuPermissionById(Long menuPermissionId) {
        try {
            logger.info("getMenuPermissionById Service Called.");
            MenuPermission menuPermission = menuPermissionRepository.findByIdAndIsDeleted(menuPermissionId, false).orElse(null);
            if(Objects.isNull(menuPermission))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Menu Permission Not Found"));
            return ResponseEntity.status(HttpStatus.OK).body(menuPermission);
        } catch (Exception e) {
            return handleException(e, "getMenuPermissionById");
        }
    }

    @Override
    public ResponseEntity<?> getAllMenuPermission() {
        try {
            logger.info("getAllMenuPermission Service Called.");
            // TODO: Have to build menu hierarchy before sending response.
            //  1. Fetch all menus from repository or Cache.
            //  2. Build hierarchy.
            //  3.Have to optimize here by fetching data from cache.
            return ResponseEntity.status(HttpStatus.OK).body(menuPermissionRepository.findAllByIsDeleted(false));
        } catch (Exception e) {
            return handleException(e, "getAllMenuPermission");
        }
    }

    @Override
    public ResponseEntity<?> getAllMenuPermissionByRole(Long roleId) {
        try {
            logger.info("getAllMenuPermissionByRole Service Called.");
            if(roleRepository.existsById(roleId)) {
                List<MenuPermission> allMenuPermissions = menuPermissionRepository.findAllByRoleIdAndIsDeleted(roleId,false);
                MenuPermissionDTO menuPermissionDTO = new MenuPermissionDTO();
                menuPermissionDTO.setRoleId(roleId);
                menuPermissionDTO.getMenuIdList().addAll(allMenuPermissions.stream()
                        .map(MenuPermission::getMenuId).collect(Collectors.toList()));
                // TODO: Have to build menu hierarchy before sending response.
                //  1. Fetch all menus from repository or Cache.
                //  2. Build hierarchy. (DONE)
                //  3.Have to optimize here by fetching data from cache.
                List<Menu> allMenus = menuRepository.findAllByIdInAndIsDeleted(menuPermissionDTO.getMenuIdList(), false);
                return ResponseEntity.status(HttpStatus.OK).body(menuServiceHelper.generateMenuForest(allMenus));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Role Not Found"));
            }
        } catch (Exception e) {
            return handleException(e, "getAllMenuPermissionByRole");
        }
    }

    @Override
    public ResponseEntity<?> updateMenuPermission(MenuPermissionDTO menuPermissionDTO) {
        try {
            logger.info("updateMenuPermission Service Called.");

            if(roleRepository.existsById(menuPermissionDTO.getRoleId())) {
                List<MenuPermission> allMenuPermissions = menuPermissionRepository
                        .findAllByRoleIdAndIsDeleted(menuPermissionDTO.getRoleId(), false);
                allMenuPermissions.forEach(menuPermission -> {
                    if(!menuPermissionDTO.getMenuIdList().contains(menuPermission.getMenuId())) {
                        menuPermission.setIsDeleted(true);
                    } else {
                        menuPermissionDTO.getMenuIdList().remove(menuPermission.getMenuId());
                    }
                });
                menuPermissionDTO.getMenuIdList().forEach(menuId -> {
                    MenuPermission menuPermission = new MenuPermission();
                    menuPermission.setRoleId(menuPermissionDTO.getRoleId());
                    menuPermission.setMenuId(menuId);
                    menuPermission.setIsDeleted(false);
                    allMenuPermissions.add(menuPermission);
                });
                menuPermissionRepository.saveAll(allMenuPermissions);
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Menu Permission Updated Successfully"));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Menu or Role Not Found"));
        } catch (Exception e) {
            return handleException(e, "updateMenuPermission");
        }
    }

    @Override
    public ResponseEntity<?> deleteMenuPermissionById(Long menuPermissionId) {
        try {
            logger.info("deleteMenuPermissionById Service Called.");
            MenuPermission menuPermission = menuPermissionRepository.findByIdAndIsDeleted(menuPermissionId, false).orElse(null);
            if(Objects.isNull(menuPermission))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Menu Permission Not Found"));
            menuPermission.setIsDeleted(true);
            menuPermissionRepository.saveAndFlush(menuPermission);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Menu Permission Deleted"));
        } catch (Exception e) {
            return handleException(e, "deleteMenuPermissionById");
        }
    }
}
