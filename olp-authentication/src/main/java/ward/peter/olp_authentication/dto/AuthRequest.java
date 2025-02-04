package ward.peter.olp_authentication.dto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AuthRequest {
	private String username;
	private String password;
}