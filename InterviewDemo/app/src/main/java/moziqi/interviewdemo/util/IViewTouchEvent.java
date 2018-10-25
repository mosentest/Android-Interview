package moziqi.interviewdemo.util;

public interface IViewTouchEvent extends ICommon {
    /**
     * 0 false,1 true ,2 super
     *
     * @param dispatch
     */
    public void setDispatchTouchEvent(int dispatch);

    public void setTouchEvent(int touchEvent);
}
