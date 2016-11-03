package com.razor.droidboard.utilities;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.razor.droidboard.interfaces.IStringItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by ruan on 5/9/2016.
 */
public class Utils
{
    public static ArrayList<String> getStringList(ArrayList<? extends IStringItem> items)
    {
        ArrayList<String> newList = new ArrayList<>();

        for (IStringItem obj : items)
        {
            newList.add(obj.getName());
        }

        return newList;
    }

    public static int getToastDuration(String Message)
    {
        int duration = 2 + (Message.split(" ").length / 3) - 1;
        duration = duration > 10 ? 10 : duration;
        return duration;
    }

    public static String getExtension(String filename)
    {
        String[] filenameArray = filename.split("\\.");
        String extension = filenameArray[filenameArray.length - 1];
        return extension;
    }

    public static int getPixFromDP(Context context, int dp)
    {
        return ((int) Math.round(dp * context.getResources().getDisplayMetrics().density));
    }

    public static boolean stringIsNullOrEmpty(String string)
    {
        if (string == null)
        {
            return true;
        }

        if (string.length() <= 0)
        {
            return true;
        }

        return false;
    }

    public static int getAge(int _year, int _month, int _day)
    {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH)) || ((m == cal.get(Calendar.MONTH)) && (d < cal.get(Calendar.DAY_OF_MONTH))))
        {
            --a;
        }
        if (a < 0)
        {
            return 0;
        }
        return a;
    }

    public static String getMimeType(String url)
    {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null)
        {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    //8001015009087
    public static Boolean validateIDNumber(String idNumber)
    {
        if (idNumber == null || idNumber.length() != 13)
        {
            return false;
        }

        int[] tmpList = new int[13];

        int index = 0;
        for (String num : idNumber.split("(?!^)"))
        {
            try
            {
                tmpList[index] = Integer.parseInt(num);
                index++;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }

        //8 + 0 + 0 + 5 + 0 + 0 = 13
        int id_1 = tmpList[0] + tmpList[2] + tmpList[4] + tmpList[6] + tmpList[8] + tmpList[10];

        //011098 * 2 = 22196
        int id_2 = ((tmpList[1] * 100000) + (tmpList[3] * 10000) + (tmpList[5] * 1000) + (tmpList[7] * 100) + (tmpList[9] * 10) + tmpList[11]) * 2;

        //2 + 2 + 1 + 9 + 6 = 20
        int id_3 = 0;
        try
        {
            for (String item : Integer.toString(id_2).split("(?!^)"))
            {
                id_3 += Integer.parseInt(item);
            }
        }
        catch (Exception e)
        {
            return false;
        }

        //13 + 20 = 33
        int id_4 = id_1 + id_3;

        //Subtract the second digit of id_4 (i.e. 3) from 10. The number must tally with the last number in the ID Number.
        String[] tmp_result = Integer.toString(id_4).split("(?!^)");

        int compare = Integer.parseInt(tmp_result[1]);

        int result = Math.abs(10 - compare);
        int checksum = 0;
        if (Integer.toString(result).length() >= 2)
        {
            String[] tmp = Integer.toString(result).split("(?!^)");
            checksum = Integer.parseInt(tmp[1]);
        }
        else
        {
            checksum = result;
        }

        return tmpList[12] == checksum;
    }

    /**
     * This will take a date time and convert the display representation to the desired pattern
     *
     * @param inDate         Date that you want to convert as String
     * @param currentPattern format of the date
     * @param outPattern     format of the date when finished
     * @return
     */
    public static String formatDateForDisplayFromString(String inDate, String currentPattern, String outPattern)
    {
        if (inDate == null || inDate.isEmpty() || currentPattern == null || currentPattern.isEmpty() || outPattern == null || outPattern.isEmpty())
        {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(currentPattern);
        try
        {
            Date date = sdf.parse(inDate);
            if (date != null)
            {
                sdf.applyPattern(outPattern);
                String stringDate = sdf.format(date);
                if (stringDate != null)
                {
                    return stringDate;
                }
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return "";
    }

    public static String formatDateForDisplayFromDate(Date inDate, String outPattern)
    {
        if (inDate != null && !TextUtils.isEmpty(outPattern))
        {
            SimpleDateFormat sdf = new SimpleDateFormat(outPattern);
            String stringDate = sdf.format(inDate);
            if (!TextUtils.isEmpty(stringDate))
            {
                return stringDate;
            }
        }

        return "";
    }

    public static Date getDateFromStringParse(String dateString, SimpleDateFormat formatter)     //SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    {
        Date date = new Date();
        try
        {
            date = formatter.parse(dateString);
            System.out.println(date);
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    public static Hashtable<String, String> getDataFromIDNumber(String idNumber)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yy");
        Date now = new Date();

        Hashtable<String, String> passback = new Hashtable<String, String>();

        String dob = idNumber.substring(0, 6);

        if (Integer.parseInt(dob.substring(0, 2)) <= Integer.parseInt(sdf.format(now)))
        {
            dob = "20" + dob;
        }
        else
        {
            dob = "19" + dob;
        }

        sdf.applyPattern("yyyyMMdd");
        passback.put("Dob", dob);

        try
        {
            int age = getDiffYears(sdf.parse(dob), now);
            passback.put("Age", Integer.toString(age));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        String tmpGender = idNumber.substring(6, 7);

        String gender = Integer.parseInt(tmpGender) < 5 ? "F" : "M";

        passback.put("Gender", gender);

        return passback;
    }

    public static int getDiffYears(Date first, Date last)
    {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) || (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE)))
        {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date)
    {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(date);
        return cal;
    }

    public static boolean validateEmail(String email)
    {
        Pattern p = Pattern.compile("^([_A-Za-z0-9-\\+])+(\\.[_A-Za-z0-9-\\+]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        return p.matcher(email).matches();
    }

    public static boolean validatePhoneNumber(String number)
    {
        number = number.replaceAll("([\\+])", "").replaceAll("([\\-])", "").replaceAll("([\\ ])", "");
        if (!TextUtils.isDigitsOnly(number))
        {
            return false;
        }

        if (number.length() < 10)
        {
            return false;
        }

        return true;
    }

    public static int dpToPx(Context context, int dp)       //more accurate...I think
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(Context context, int px)       //more accurate...I think
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static String getApplicationName(Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }
}
