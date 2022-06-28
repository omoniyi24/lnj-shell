package com.omoniyi24.lnjshell;


import org.ldk.enums.ConfirmationTarget;
import org.ldk.structs.FeeEstimator;

/**
 * @author OMONIYI ILESANMI
 */
public class FeeEstimatorService implements FeeEstimator.FeeEstimatorInterface{

    @Override
    public int get_est_sat_per_1000_weight(ConfirmationTarget confirmationTarget) {
        int fee = 0;
        switch (confirmationTarget) {
            case LDKConfirmationTarget_Background:
                fee = 253;
                break;
            case LDKConfirmationTarget_Normal:
                fee = 2000;
                break;
            case LDKConfirmationTarget_HighPriority:
                fee = 5000;
                break;
            default:
                System.out.println("[-] Fee other than 253, 2000 or 5000");
        }
        return fee;
    }
}
