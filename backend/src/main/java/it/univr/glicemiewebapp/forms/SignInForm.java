package it.univr.glicemiewebapp.forms;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class SignInForm {
  private String email;
  private String password;
}
