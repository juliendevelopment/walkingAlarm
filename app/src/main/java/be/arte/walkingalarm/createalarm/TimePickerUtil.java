package be.arte.walkingalarm.createalarm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Build;
import android.widget.EditText;
import android.widget.TimePicker;

public final class TimePickerUtil {
    public static int getTimePickerHour(TimePicker tp) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tp.getHour();
        } else {
            return tp.getCurrentHour();
        }
    }

    public static int getTimePickerMinute(TimePicker tp) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tp.getMinute();
        } else {
            return tp.getCurrentMinute();
        }
    }

    public static int getTimeHour(EditText editText) {
        Date date = getDate(editText);
        return date.getHours();
    }


    public static int getTimeMinute(EditText editText) {
        Date date = getDate(editText);
        return date.getMinutes();
    }

    private static Date getDate(EditText editText) {
        try {
        DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
            return formatter.parse(editText.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
