package org.telbot.telran.info.service;

import org.telbot.telran.info.exceptions.NoDataFoundException;
import org.telbot.telran.info.exceptions.NoStatisticFoundException;
import org.telbot.telran.info.model.Statistic;

public interface StatisticService {

    void createReport() throws NoDataFoundException;

    Statistic getLastStatisticReport() throws NoStatisticFoundException;

}
