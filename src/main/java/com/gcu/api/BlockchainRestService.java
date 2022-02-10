//package com.gcu.api;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.gcu.business.BlockchainBusinessService;
//import com.gcu.business.ProductBusinessService;
//import com.gcu.controller.rainfall;
//import com.gcu.data.BlockchainDataService;
//import com.gcu.data.UserDataService;
//import com.gcu.model.Block;
//import com.gcu.model.ProductModel;
//import com.gcu.model.UserModel;
//import com.gcu.model.Wallet;
//import com.gcu.transaction.TransactionOutput;
//
//
///**
// * Returns the JSON API
// */
//@RestController
//@RequestMapping("/service")
//public class BlockchainRestService 
//{
//	@Autowired
//	BlockchainDataService service;
//	
//	/** Displays a list of products in JSON format
//	 * @return JSON view of products
//	 * @throws SQLException 
//	 */
//	@GetMapping(path="/blocks")
//	public void getOrdersAsJSON() throws SQLException
//	{
//		rainfall.test();
//		List<Block> blockchain = new ArrayList<Block>();
//		blockchain.add(new Block("0"));
//		Wallet walletA = new Wallet();
//		
//		try
//		{
//			//Create a new list of products and populate it with all products in the database
//			
//			//if products is null, return HTTP Not Found entity
//			if(blockchain.size() == 0) {
//				System.out.println("RESPONSEENtitY NULL OR SOMETHING IDK");
//			}
//			// else return HTTP Ok response entity and products in json format
//			else
//			{
//				System.out.println("=====================");
//				HashMap<String, TransactionOutput> utxos = service.readUTXOs();
//				for (String id : utxos.keySet()) {
//					TransactionOutput to = new TransactionOutput();
//					String key = id.toString();
//					to = utxos.get(key);
//					System.out.println("ID: " + to.getId());
//					System.out.println("Value: " + to.getValue());
//					System.out.println("Recipient: " + to.getReciepient().getEncoded());
//				}
//			}
//		}
//		catch (Exception e)
//		{
//			System.out.println("exception");
//		}
//		
//	}
//
//}
//
