package com.bc.bookcrossing.src.View.Fragment;

import com.bc.bookcrossing.src.UnitTest.NoScanResultException;

/**
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public interface ScanResultReceiver {
    public void scanResultData(String codeFormat, String codeContent);

    public void scanResultData(NoScanResultException noScanData);
}
