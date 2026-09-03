package com.activachilecito.core.dto;

import com.activachilecito.core.model.Rol;
import lombok.Data;
import java.util.Set;

@Data
public class ActualizarRolesRequest {
    private Set<Rol> roles;
}
