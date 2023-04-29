package io.findi.dao.controller;

import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH;

import java.math.BigInteger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.sample.contracts.generated.SocialLock;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

@RestController
public class DaoController {
	static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
	static final BigInteger GAS_LIMIT = BigInteger.valueOf(1_000_000_000L);
	private Web3j web3j;
	final Credentials creator = Credentials.create("0x606e2afc79533d5f22272ce132ef8eeee5a0f6620495aa7b6d914552293f8997");
	private DefaultGasProvider contractGasProvider;
	public DaoController() {
		web3j = Web3j.build(new HttpService("https://eth.findi.biz"));
		this.contractGasProvider = new DefaultGasProvider();
	}
	@RequestMapping("/deploy")
	public SocialLock deploy() throws Exception {
		SocialLock token = SocialLock.deploy(
                web3j,
               creator,
                contractGasProvider
                ,BigInteger.valueOf(0L),
                "jason"
				)
                .send();
		return token;
	}
	
	@RequestMapping("/mint")
	public BigInteger mint() throws Exception {
		BigInteger token = SocialLock.load("0x4fbeb3e064dadc9b7214c0b5c27075733b9f4e37",
                web3j,
                new RawTransactionManager(web3j, creator, 19820518, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH, 200),
                contractGasProvider).balances("".getBytes()).send();
		return token;
	}
}
