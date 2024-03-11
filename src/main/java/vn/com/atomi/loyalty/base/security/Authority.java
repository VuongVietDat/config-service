package vn.com.atomi.loyalty.base.security;

/**
 * @author haidv
 * @version 1.0
 */
public class Authority {

  public static final String ROLE_SYSTEM = "hasAuthority('ROLE_SYSTEM')";

  public static class Rule {
    public static final String CREATE_RULE = "hasAuthority('CREATE_RULE')";
    public static final String UPDATE_RULE = "hasAuthority('UPDATE_RULE')";
    public static final String READ_RULE = "hasAuthority('READ_RULE')";
    public static final String APPROVE_RULE = "hasAuthority('APPROVE_RULE')";
  }
}
