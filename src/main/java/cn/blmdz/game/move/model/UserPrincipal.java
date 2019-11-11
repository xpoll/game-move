package cn.blmdz.game.move.model;

import java.security.Principal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements Principal {

	private Long id;
	private String name;

	@Override
	public String getName() {
		return this.name;
	}
}
