//package org.firstinspires.ftc.teamcode.tune;
//
//import com.acmerobotics.dashboard.config.ValueProvider;
//import com.acmerobotics.dashboard.config.variable.ConfigVariable;
//import com.acmerobotics.dashboard.config.variable.CustomVariable;
//import com.acmerobotics.dashboard.config.variable.VariableType;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.ServoImplEx;
//
//public class DashHardware {
//    private static abstract class OptionalDoubleValueProvider implements ValueProvider<String> {
//        String input;
//
//        public abstract void setSome(double v);
//        public abstract void setNone();
//
//        @Override
//        public String get() {
//            return input;
//        }
//
//        @Override
//        public void set(String value) {
//            if (value.equals("")) {
//                this.setNone();
//            } else {
//                double x;
//                try {
//                    x = Double.parseDouble(value);
//                } catch (NumberFormatException e) {
//                    return;
//                }
//                this.setSome(x);
//            }
//            input = value;
//        }
//    }
//
//    private static class DcMotorValueProvider extends OptionalDoubleValueProvider {
//        DcMotorEx inner;
//
//        public DcMotorValueProvider(DcMotorEx inner) {
//            this.inner = inner;
//        }
//
//        @Override
//        public void setSome(double v) {
//            inner.setMotorEnable();
//            inner.setPower(v);
//        }
//
//        @Override
//        public void setNone() {
//            inner.setMotorDisable();
//        }
//    }
//
//    private static class ServoValueProvider extends OptionalDoubleValueProvider {
//        ServoImplEx inner;
//
//        public ServoValueProvider(ServoImplEx inner) {
//            this.inner = inner;
//        }
//
//        @Override
//        public void setSome(double v) {
//            inner.setPwmEnable();
//            inner.setPosition(v);
//        }
//
//        @Override
//        public void setNone() {
//            inner.setPwmDisable();
//        }
//    }
//
//    private static class HardwareVariable extends ConfigVariable<Object> {
//        HardwareMap hw;
//
//        public HardwareVariable(HardwareMap hw) {
//            this.hw = hw;
//
//            hw.
//        }
//
//        @Override
//        public VariableType getType() {
//            return VariableType.CUSTOM;
//        }
//
//        @Override
//        public Object getValue() {
//            return null;
//        }
//
//        @Override
//        public void update(ConfigVariable<Object> newVariable) {
//            CustomVariable diff = (CustomVariable)newVariable;
//        }
//    }
//}
