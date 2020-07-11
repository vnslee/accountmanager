package com.kakaopay.sec.repository;

import java.util.List;

import com.kakaopay.sec.model.entity.Transaction;

/**
 * 거래 내역 관리 Repository
 */
public interface TransactionRepository {

    /**
     * 모든 거래 내역을 제거한다.
     */
    void deleteAll();
    
    /**
     * 모든 거래 내역을 반환한다.
     * 
     * @return 등록된 모든 거래 내역
     */
    List<Transaction> findAll();
    
    /**
     * 입력된 계좌번호의 거래 내역을 반환한다.
     * 
     * @param acctNo 계좌 번호
     * @return 거래 내역 목록
     */
    List<Transaction> findById(String acctNo);
    
    /**
     * 입력된 거래 내역을 등록한다.
     * @param transaction 거래 내역
     */
    void save(Transaction transaction);
    
    /**
     * 입력된 거래 내역을 등록한다.
     * 
     * @param transactions 거래 내역
     */
    void saveAll(List<? extends Transaction> transactions);
}
