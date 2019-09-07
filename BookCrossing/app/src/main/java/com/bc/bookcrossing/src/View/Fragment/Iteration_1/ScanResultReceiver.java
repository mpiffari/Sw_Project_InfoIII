package com.bc.bookcrossing.src.View.Fragment.Iteration_1;

import com.bc.bookcrossing.src.ClientModels.NoScanResultException;

/**
 *
 * Interfaccia per la gestione della scansione dell'ISBN e dei suoi risultati.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public interface ScanResultReceiver {
    public void scanResultData(String codeFormat, String codeContent);
    public void scanResultData(NoScanResultException noScanData);
}
