package seedu.address.model.account;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * AccountList represents the entire list of accounts that were created
 * and stored in the local database. Currently, all username and password are
 * stored locally in a xml file without encryption.
 */
public class AccountList {
    private List<Account> accountList;

    public AccountList() {
        accountList = new ArrayList<>();
    }

    public List<Account> getList() {
        return accountList;
    }

    public void addAccount(Account account) {
        accountList.add(account);
    }

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasAccount(Account account) {
        requireNonNull(account);
        return accountList.contains(account);
    }

    public boolean hasUserName(String username) {
        requireNonNull(username);
        for(Account account : accountList) {
            if (account.getUserName().equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }
}
