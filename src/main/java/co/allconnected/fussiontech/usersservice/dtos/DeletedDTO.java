// src/main/java/co/allconnected/fussiontech/usersservice/dtos/DeletedDTO.java
package co.allconnected.fussiontech.usersservice.dtos;

import java.io.Serializable;
import java.time.Instant;

public record DeletedDTO(String id_user, String delete_reason, Instant delete_date) implements Serializable {
}