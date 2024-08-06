package com.reve.authentication.server.menu;

import com.reve.authentication.server.common.RootRepository;

import java.util.List;

public interface MenuRepository extends RootRepository<Menu, Long> {
    public Menu findByHyperLinkAndIsDeleted(String hyperlink, Boolean isDeleted);

}
