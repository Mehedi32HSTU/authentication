package com.reve.authentication.server.role;

import com.reve.authentication.server.common.ExceptionResponseHandler;
import com.reve.authentication.server.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RoleServiceImpl implements RoleService, ExceptionResponseHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ResponseEntity<?> addRole(Role role) {
        try {
            logger.info("addRole Service Called.");
            if(Objects.nonNull(roleRepository.findByNameIgnoreCaseAndIsDeleted(role.getName(), false)))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Role Name Already Exists."));

            role.setIsDeleted(false);
            return ResponseEntity.status(HttpStatus.OK).body(roleRepository.saveAndFlush(role));
        } catch (Exception e) {
            e.printStackTrace();
            return handleException(e, "addRole");
        }
    }

    @Override
    public ResponseEntity<?> getRoleById(Long roleId) {
        try {
            logger.info("getRoleById Service Called.");
            Role role = roleRepository.findByIdAndIsDeleted(roleId, false).orElse(null);
            if(Objects.isNull(role))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Role Not Found"));
            return ResponseEntity.status(HttpStatus.OK).body(role);
        } catch (Exception e) {
            e.printStackTrace();
            return handleException(e, "getRoleById");
        }
    }

    @Override
    public ResponseEntity<?> getAllRole() {
        try {
            logger.info("getAllRole Service Called.");
            return ResponseEntity.status(HttpStatus.OK).body(roleRepository.findAllByIsDeleted(false));
        } catch (Exception e) {
            e.printStackTrace();
            return handleException(e, "getAllRole");
        }
    }

    @Override
    public ResponseEntity<?> updateRole(Long roleId, Role newRoleReq) {
        try {
            logger.info("updateRole Service Called.");
            // TODO: Have to think about roleNameEng and roleNameBng
            Role oldRole = roleRepository.findByIdAndIsDeleted(roleId, false).orElse(null);

            if(Objects.isNull(oldRole))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Role Not Found"));
            Role validityCheckRole = roleRepository.findByNameIgnoreCaseAndIsDeleted(newRoleReq.getName(), false);
            if(Objects.nonNull(validityCheckRole) && !Objects.equals(oldRole.getId(), validityCheckRole.getId()))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Role Name Already Exists."));

            oldRole.setName(newRoleReq.getName());
            oldRole.setRoleNameEng(newRoleReq.getRoleNameEng());
            oldRole.setRoleNameBng(newRoleReq.getRoleNameBng());
            return ResponseEntity.status(HttpStatus.OK).body(roleRepository.saveAndFlush(oldRole));
        } catch (Exception e) {
            e.printStackTrace();
            return handleException(e, "updateRole");
        }
    }

    @Override
    public ResponseEntity<?> deleteRoleById(Long roleId) {
        try {
            logger.info("deleteRoleById Service Called.");
            Role role = roleRepository.findByIdAndIsDeleted(roleId, false).orElse(null);
            if(Objects.isNull(role))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Role Not Found"));
            role.setIsDeleted(true);
            roleRepository.saveAndFlush(role);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Role Deleted"));
        } catch (Exception e) {
            e.printStackTrace();
            return handleException(e, "deleteRoleById");
        }
    }
}
