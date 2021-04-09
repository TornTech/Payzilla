package ui;

import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.ParseException;

// Modified class of the NumberFormatter which allows to delete integers
// after they have been typed in and only allows integers
public class IntegerFormatter extends NumberFormatter {

    public IntegerFormatter(NumberFormat format) {
        super(format);
        setValueClass(Integer.class);
        setMinimum(0);
        setMaximum(Integer.MAX_VALUE);
        setAllowsInvalid(false);
    }

    // EFFECTS: Allows number input to have no values inputted
    //          Otherwise, will not allow to remove last integer
    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text.equals("")) {
            return null;
        } else {
            return super.stringToValue(text);
        }
    }

}
