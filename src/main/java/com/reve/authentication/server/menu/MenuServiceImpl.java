package com.reve.authentication.server.menu;

import com.reve.authentication.server.common.ExceptionResponseHandler;
import com.reve.authentication.server.menu.permision.MenuPermission;
import com.reve.authentication.server.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MenuServiceImpl implements MenuService, ExceptionResponseHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuServiceHelper menuServiceHelper;

    @Override
    public ResponseEntity<?> addMenu(Menu menu) {
        try {
            logger.info("addMenu Service Called.");
            if(Objects.nonNull(menuRepository.findByHyperLinkAndIsDeleted(menu.getHyperLink(), false)))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Hyperlink Already Exists."));

            menu.setIsDeleted(false);
            return ResponseEntity.status(HttpStatus.OK).body(menuRepository.saveAndFlush(menu));
        } catch (Exception e) {
            return handleException(e, "addMenu");
        }
    }

    @Override
    public ResponseEntity<?> getMenuById(Long menuId) {
        try {
            logger.info("getMenuById Service Called.");
            Menu menu = menuRepository.findByIdAndIsDeleted(menuId, false).orElse(null);
            if(Objects.isNull(menu))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Menu Not Found"));
            return ResponseEntity.status(HttpStatus.OK).body(menu);
        } catch (Exception e) {
            return handleException(e, "getMenuById");
        }
    }

    @Override
    public ResponseEntity<?> getAllMenu() {
        try {
            logger.info("getAllMenu Service Called.");
            // TODO: Have to build menu hierarchy before sending response.
            //  1. Fetch all menus from repository or Cache.
            //  2. Build hierarchy. (DONE)
            //  3.Have to optimize here by fetching data from cache.
            List<Menu> allMenus = menuRepository.findAllByIsDeleted(false);
            return ResponseEntity.status(HttpStatus.OK).body(menuServiceHelper.generateMenuForest(allMenus));
        } catch (Exception e) {
            return handleException(e, "getAllMenu");
        }
    }

    @Override
    public ResponseEntity<?> getAllMenuByRole(Long roleId) {
        try {
            logger.info("getAllMenuByRole Service Called.");
            // TODO: Have to implement more functionalities.
            return ResponseEntity.status(HttpStatus.OK).body(menuRepository.findAllByIsDeleted(false));
        } catch (Exception e) {
            return handleException(e, "getAllMenuByRole");
        }
    }

    @Override
    public ResponseEntity<?> updateMenu(Long menuId, Menu newMenuReq) {
        try {
            logger.info("updateMenu Service Called.");
            Menu oldMenu = menuRepository.findByIdAndIsDeleted(menuId, false).orElse(null);

            if(Objects.isNull(oldMenu))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Menu Not Found"));
            Menu validityCheckMenu = menuRepository.findByHyperLinkAndIsDeleted(newMenuReq.getHyperLink(), false);
            if(Objects.nonNull(validityCheckMenu) && !Objects.equals(oldMenu.getId(), validityCheckMenu.getId()))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Hyperlink Already Exists."));

            oldMenu.setParentMenuId(newMenuReq.getParentMenuId());
            oldMenu.setMenuNameBng(newMenuReq.getMenuNameBng());
            oldMenu.setMenuNameEng(newMenuReq.getMenuNameEng());
            oldMenu.setIcon(newMenuReq.getIcon());
            oldMenu.setHyperLink(newMenuReq.getHyperLink());
            oldMenu.setConstantName(newMenuReq.getConstantName());
            oldMenu.setOrderIndex(newMenuReq.getOrderIndex());
            oldMenu.setIsVisible(newMenuReq.getIsVisible());
            oldMenu.setIsAPI(newMenuReq.getIsAPI());
            oldMenu.setRequestMethod(newMenuReq.getRequestMethod());

            return ResponseEntity.status(HttpStatus.OK).body(menuRepository.saveAndFlush(oldMenu));
        } catch (Exception e) {
            return handleException(e, "updateMenu");
        }
    }

    @Override
    public ResponseEntity<?> deleteMenuById(Long menuId) {
        try {
            logger.info("deleteMenuById Service Called.");
            Menu menu = menuRepository.findByIdAndIsDeleted(menuId, false).orElse(null);
            if(Objects.isNull(menu))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Menu Not Found"));
            menu.setIsDeleted(true);
            menuRepository.saveAndFlush(menu);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Menu Deleted"));
        } catch (Exception e) {
            return handleException(e, "deleteMenuById");
        }
    }
}
