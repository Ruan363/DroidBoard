package com.razor.droidboard.models;

import android.accounts.Account;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.razor.droidboard.R;

/**
 * Created by ruan on 5/9/2016.
 */
public class User extends SugarRecord implements Serializable
{
    public final static Integer SYNC_CONTENT_QUOTES = 1, SYNC_CONTENT_ALL = 2;

    private String password = null;
    private String username = null;
    @Ignore
    private Set types = null;
    private String cellularNumber = null;
    private String lastname = null;
    private String pin = null;
    private String email = null;
    private String firstname = null;
    private Set addresses = null;
    private Date lastLogin = null;
    private Long syncInterval = null;
    private Boolean syncEnabled = null;
    private Integer syncContent = null;

    @Ignore
    private static final String TAG = "Client (db)";

    public User(){}

    /**
     **/
//
//  @ApiModelProperty(value = "")
//  @JsonProperty("id")
//  public Long getId() {
//    return id;
//  }
//  public void setId(Long id) {
//    this.id = id;
//  }
//
//


    public Integer getSyncContent()
    {
        return syncContent;
    }

    public void setSyncContent(Integer syncContent)
    {
        this.syncContent = syncContent;
    }

    public Boolean getSyncEnabled()
    {
        return syncEnabled;
    }

    public void setSyncEnabled(Boolean syncEnabled)
    {
        this.syncEnabled = syncEnabled;
    }

    public Long getSyncInterval()
    {
        return syncInterval;
    }

    public void setSyncInterval(Long syncInterval)
    {
        this.syncInterval = syncInterval;
    }

    /**
     **/

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }


    /**
     **/

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }


    /**
     **/
    public Set getTypes()
    {
        return types;
    }

    public void setTypes(Set types)
    {
        this.types = types;
    }


    /**
     **/
    public String getCellularNumber()
    {
        return cellularNumber;
    }

    public void setCellularNumber(String cellularNumber)
    {
        this.cellularNumber = cellularNumber;
    }


    /**
     **/
    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }


    /**
     **/
    public String getPin()
    {
        return pin;
    }

    public void setPin(String pin)
    {
        this.pin = pin;
    }


    /**
     **/
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }


    /**
     **/

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }


    /**
     **/

    public Set getAddresses()
    {
        return addresses;
    }

    public void setAddresses(Set addresses)
    {
        this.addresses = addresses;
    }


    /**
     **/
    public Date getLastLogin()
    {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin)
    {
        this.lastLogin = lastLogin;
    }


    /**
     **/


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        User user = (User) o;
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("class User {\n");

        sb.append("    id: ").append(toIndentedString(getId())).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    types: ").append(toIndentedString(types)).append("\n");
        sb.append("    cellularNumber: ").append(toIndentedString(cellularNumber)).append("\n");
        sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
        sb.append("    pin: ").append(toIndentedString(pin)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
        sb.append("    addresses: ").append(toIndentedString(addresses)).append("\n");
        sb.append("    lastLogin: ").append(toIndentedString(lastLogin)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o)
    {
        if (o == null)
        {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    public static String getAuthorizationHeader()
    {
        ArrayList<User> currUsers = (ArrayList<User>) SugarRecord.listAll(User.class);
        if (currUsers == null || currUsers.isEmpty())
            return null;
        User currUser = currUsers.get(0);
        String encrypedUserDetails = "";
        try {
            encrypedUserDetails = Base64.encodeToString((currUser.getUsername().toString() + ":" + currUser.getPassword().toString()).getBytes("UTF-8"), Base64.NO_WRAP);
            Log.i(TAG, "Auth: " + encrypedUserDetails);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ("Basic "+encrypedUserDetails);
    }

    public static Boolean checkUserLoggedIn()
    {
        long amount = User.count(User.class, null, null);
        if (amount > 0)
            return true;
        else
            return false;
    }

    public static User getUser()
    {
        User user = User.first(User.class);
        if (user == null)
            return null;

        return user;
    }

    public static void setUser(User user)
    {
        if (user != null)
        {
            User.deleteAll(User.class);

            User.save(user);
        }
    }

    public static void clearUser()
    {
        User.deleteAll(User.class);
    }

    public static Account getUserAccount(Context context)
    {
        String accountName = User.getUser().getUsername();

        Account newAccount = new Account(accountName, context.getResources().getString(R.string.sync_account_type));

        return newAccount;
    }

    public static void updateTimeInterval(Long time)
    {
        User user = getUser();
        user.setSyncInterval(time);
        user.save();
//        User userx = getUser();
    }
}
