package co.allconnected.fussiontech.usersservice.dtos;

import co.allconnected.fussiontech.usersservice.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class InactiveUserDTO extends UserDTO{
    private String delete_reason;
    private Instant delete_date;
    public InactiveUserDTO(User user){
        super(user);
        this.delete_reason = user.getDeleted().getReason();
        this.delete_date = user.getDeleted().getDeleteDate();
    }
}
