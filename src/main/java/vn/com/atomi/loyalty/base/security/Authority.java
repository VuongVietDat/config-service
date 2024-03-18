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

  public static class Campaign {
    public static final String CREATE_CAMPAIGN = "hasAuthority('CREATE_CAMPAIGN')";
    public static final String UPDATE_CAMPAIGN = "hasAuthority('UPDATE_CAMPAIGN')";
    public static final String READ_CAMPAIGN = "hasAuthority('READ_CAMPAIGN')";
    public static final String APPROVE_CAMPAIGN = "hasAuthority('APPROVE_CAMPAIGN')";
  }

  public static class CustomerGroup {
    public static final String CREATE_CUSTOMER_GROUP = "hasAuthority('CREATE_CUSTOMER_GROUP')";
    public static final String UPDATE_CUSTOMER_GROUP = "hasAuthority('UPDATE_CUSTOMER_GROUP')";
    public static final String READ_CUSTOMER_GROUP = "hasAuthority('READ_CUSTOMER_GROUP')";
    public static final String APPROVE_CUSTOMER_GROUP = "hasAuthority('APPROVE_CUSTOMER_GROUP')";
  }

  public static class Rank {
    public static final String CREATE_RANK = "hasAuthority('CREATE_RANK')";
    public static final String UPDATE_RANK = "hasAuthority('UPDATE_RANK')";
    public static final String READ_RANK = "hasAuthority('READ_RANK')";
    public static final String APPROVE_RANK = "hasAuthority('APPROVE_RANK')";
  }

  public static class Product {
    public static final String READ_PRODUCT = "hasAuthority('READ_PRODUCT')";
  }
}
