package com.bc.bookcrossing.bookcrossing.GUI.Fragment;

public interface ScanResultReceiver {
    public void scanResultData(String codeFormat, String codeContent);

    public void scanResultData(NoScanResultException noScanData);
}
