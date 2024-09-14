// src/main/java/co/allconnected/fussiontech/usersservice/dtos/DeletedDTO.java
package co.allconnected.fussiontech.usersservice.dtos;

import java.io.Serializable;
import java.time.Instant;

public record DeletedDTO(String idUser, String reason, Instant deleteDate) implements Serializable {
}