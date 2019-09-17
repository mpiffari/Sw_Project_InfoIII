package com.bc.bookcrossing.src.GUI.Observer;

import com.bc.bookcrossing.src.ClientModels.BookInfo;
import java.util.ArrayList;

/**
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public interface ObserverDataBookTaken extends ObserverForUiInformation {
    void notifyBookTaken(ArrayList<BookInfo> bookInformations);
}
