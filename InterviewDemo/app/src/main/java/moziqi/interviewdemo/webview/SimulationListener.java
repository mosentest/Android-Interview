package moziqi.interviewdemo.webview;

/**
 * Copyright (C), 2018-2018
 * FileName: SimulationListener
 * Author: ziqimo
 * Date: 2018/11/29 下午9:39
 * Description: ${DESCRIPTION}
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public interface SimulationListener {
    public void doSimulation();

    public void onPageFinished(String url);

    public void onError(String url);
}
