package com.bc.bookcrossing.src.View.Observer;

import com.bc.bookcrossing.src.ClientModels.Book;
import java.util.List;

/**
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public interface ObserverDataBookResearch extends ObserverForUiInformation {
    void notifyBookSearch(boolean result, List<Book> books);
}
