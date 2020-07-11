package com.kakaopay.sec.service;

import java.util.List;
import java.util.Set;

import com.kakaopay.sec.model.entity.Transaction;

public interface TransactionService {

    /**
     * 등록된 모든 거래 내역 목록을 반환한다.
     * 
     * @return 모든 거래 내역 목록
     */
    List<Transaction> findAll();
    
    /**
     * 입력된 계좌 번호의 거래 내역 목록을 반환한다.
     * 
     * @param acctNo 계좌 번호
     * @return 거래 내역 목록
     */
    List<Transaction> findByAcctNo(String acctNo);
    
    /**
     * 거래 내역이 포함된 연도 목록을 반환한다.
     * 
     * @return 연도 목록
     */
    Set<Integer> getAllTransacionYears();
    
}
