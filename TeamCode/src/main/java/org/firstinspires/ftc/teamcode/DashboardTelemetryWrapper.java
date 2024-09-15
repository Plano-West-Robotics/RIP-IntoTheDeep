package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.units.Distance2;
import org.firstinspires.ftc.teamcode.units.Pose;

public class DashboardTelemetryWrapper implements Telemetry {
    private final FtcDashboard dashboard;
    private TelemetryPacket packet;
    private boolean canvasHoldingStaleData;
    private final TelemetryLog log;
    private final Object lock = new Object();

    private static final Canvas DEFAULT_FIELD = new TelemetryPacket().fieldOverlay();

    public DashboardTelemetryWrapper(FtcDashboard dashboard) {
        this.dashboard = dashboard;
        this.packet = new TelemetryPacket();
        this.canvasHoldingStaleData = false;
        this.log = this.new TelemetryLog();
    }

    private Canvas getCanvasForDrawing() {
        Canvas canvas = this.packet.fieldOverlay();

        if (this.canvasHoldingStaleData) {
            canvas.clear();
            canvas.getOperations().addAll(DEFAULT_FIELD.getOperations());
            this.canvasHoldingStaleData = false;
        }

        return canvas;
    }

    private void rawDrawRobot(Pose pose) {
        Distance2 pos = pose.pos.mul(24 / Distance.IN_PER_TILE);

        // 2---3    6---7
        // |   |    |   |
        // |   4----5   |
        // |            |
        // |            |
        // 1------------8
        Distance2[] ps = new Distance2[]{
                new Distance2( Distance.ROBOT_LENGTH.neg(),  Distance.ROBOT_WIDTH              ).div(2),
                new Distance2( Distance.ROBOT_LENGTH,        Distance.ROBOT_WIDTH              ).div(2),
                new Distance2( Distance.ROBOT_LENGTH,        Distance.ROBOT_WIDTH.div(2)       ).div(2),
                new Distance2( Distance.ROBOT_LENGTH.div(5), Distance.ROBOT_WIDTH.div(2)       ).div(2),
                new Distance2( Distance.ROBOT_LENGTH.div(5), Distance.ROBOT_WIDTH.neg().div(2) ).div(2),
                new Distance2( Distance.ROBOT_LENGTH,        Distance.ROBOT_WIDTH.neg().div(2) ).div(2),
                new Distance2( Distance.ROBOT_LENGTH,        Distance.ROBOT_WIDTH.neg()        ).div(2),
                new Distance2( Distance.ROBOT_LENGTH.neg(),  Distance.ROBOT_WIDTH.neg()        ).div(2)
        };

        double[] xs = new double[ps.length];
        double[] ys = new double[ps.length];
        for (int i = 0; i < ps.length; i ++) {
            Distance2 p = ps[i].rot(pose.yaw).add(pos);
            xs[i] = p.x.valInInches();
            ys[i] = p.y.valInInches();
        }

        Canvas canvas = this.getCanvasForDrawing();
        canvas.fillPolygon(xs, ys);
        canvas.strokePolygon(xs, ys);
    }

    public void drawRobot(Pose pose) {
        synchronized (lock) {
            Canvas canvas = this.getCanvasForDrawing();
            canvas.setStrokeWidth(1);
            canvas.setFill("gray");
            canvas.setStroke("black");

            this.rawDrawRobot(pose);
        }
    }

    public void drawTarget(Pose target, Pose current) {
        synchronized (lock) {
            Canvas canvas = this.getCanvasForDrawing();
            canvas.setStrokeWidth(1);
            canvas.setFill("lightgreen");
            canvas.setStroke("lightgreen");

            canvas.fillCircle(target.pos.x.valInInches(), target.pos.y.valInInches(), 1);
            canvas.strokeLine(
                    current.pos.x.valInInches(),
                    current.pos.y.valInInches(),
                    target.pos.x.valInInches(),
                    target.pos.y.valInInches()
            );

            canvas.setFill("transparent");
            this.rawDrawRobot(target);
        }
    }

    @Override
    public Item addData(String caption, String format, Object... args) {
        return this.addData(caption, String.format(format, args));
    }

    @Override
    public Item addData(String caption, Object value) {
        this.packet.put(caption, value);
        return null;
    }

    @Override
    public <T> Item addData(String caption, Func<T> valueProducer) {
        return this.addData(caption, valueProducer.value());
    }

    @Override
    public <T> Item addData(String caption, String format, Func<T> valueProducer) {
        return this.addData(caption, format, valueProducer.value());
    }

    @Override
    public boolean removeItem(Item item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        this.dashboard.clearTelemetry();
        this.packet = new TelemetryPacket();
    }

    @Override
    public void clearAll() {
        this.clear();
    }

    @Override
    public Object addAction(Runnable action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAction(Object token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void speak(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void speak(String text, String languageCode, String countryCode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean update() {
        synchronized (lock) {
            TelemetryPacket oldPacket = this.packet;
            this.packet = new TelemetryPacket();
            this.packet.fieldOverlay().clear();
            this.packet.fieldOverlay().getOperations().addAll(oldPacket.fieldOverlay().getOperations());
            this.canvasHoldingStaleData = true;
            this.dashboard.sendTelemetryPacket(oldPacket);
            return true;
        }
    }

    @Override
    public Line addLine() {
        return null;
    }

    @Override
    public Line addLine(String lineCaption) {
        this.packet.addLine(lineCaption);
        return null;
    }

    @Override
    public boolean removeLine(Line line) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAutoClear() {
        return false;
    }

    @Override
    public void setAutoClear(boolean autoClear) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMsTransmissionInterval() {
        return this.dashboard.getTelemetryTransmissionInterval();
    }

    @Override
    public void setMsTransmissionInterval(int msTransmissionInterval) {
        this.dashboard.setTelemetryTransmissionInterval(msTransmissionInterval);
    }

    @Override
    public String getItemSeparator() {
        return null;
    }

    @Override
    public void setItemSeparator(String itemSeparator) {

    }

    @Override
    public String getCaptionValueSeparator() {
        return null;
    }

    @Override
    public void setCaptionValueSeparator(String captionValueSeparator) {

    }

    @Override
    public void setDisplayFormat(DisplayFormat displayFormat) {

    }

    @Override
    public Log log() {
        return log;
    }

    private class TelemetryLog implements Log {
        @Override
        public int getCapacity() {
            return 0;
        }

        @Override
        public void setCapacity(int capacity) {

        }

        @Override
        public DisplayOrder getDisplayOrder() {
            return DisplayOrder.OLDEST_FIRST;
        }

        @Override
        public void setDisplayOrder(DisplayOrder displayOrder) {

        }

        @Override
        public void add(String entry) {
            packet.addLine(entry);
        }

        @Override
        public void add(String format, Object... args) {
            this.add(String.format(format, args));
        }

        @Override
        public void clear() {
            packet.clearLines();
        }
    }
}
