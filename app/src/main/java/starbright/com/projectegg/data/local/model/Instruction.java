package starbright.com.projectegg.data.local.model;

public class Instruction {

    private String mNumber;
    private String mStep;

    public Instruction(String number, String step) {
        mNumber = number;
        mStep = step;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getStep() {
        return mStep;
    }

    public void setStep(String step) {
        mStep = step;
    }
}
