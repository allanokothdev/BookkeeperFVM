/* eslint-disable require-jsdoc */
/* eslint-disable no-unused-vars */
/* eslint-disable max-len */

const ethers = require("ethers");

// Firebase
const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

// FEVM RPC
const FilecoinRpc = "https://api.hyperspace.node.glif.io/rpc/v1";
const provider = new ethers.providers.JsonRpcProvider(FilecoinRpc);

// SMART CONTRACT DETAILS
const BookkeeperABI = require("./abi/Bookkeeper.json");
const BookkeeperContractAddress = "0x049613465E605e541fcd49eA94beb9d1d7220378";


// CREATE NEW WALLET ADDRESS
exports.createWallet = functions.https.onCall(() => {
  const wallet = ethers.Wallet.createRandom();
  console.log(wallet.address);
  return {
    address: wallet.address,
    privateKey: wallet.privateKey,
  };
});

// FETCH Business Stock Records
exports.fetchStocks = functions.https.onCall(async (_data, context) => {
  try {
    const privateKey = _data.key;
    const signer = new ethers.Wallet(privateKey, provider);
    const bookkeeperContract = new ethers.Contract(BookkeeperContractAddress, BookkeeperABI, signer);
    // fetching the business records using the from the Bookkeeper contracts
    const response = await bookkeeperContract.fetchStocks();
    const records = [];
    await Promise.all(response.map(async (i) => {
      const item = {
        recordId: i.recordId.toNumber(),
        title: i.title,
        recordType: i.recordType,
        timestamp: i.timestamp,
        recordDate: i.recordDate.toNumber(),
        price: i.price.toNumber(),
        quantity: i.quantity.toNumber(),
        salePrice: i.salePrice.toNumber(),
        brandID: i.brandID.toString(),
      };
      records.push(item);
    }));

    // RETURN LIST TO THE UI
    return records;
  } catch (error) {
    console.error(error);
    // Re-throwing the error as an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError("unknown", error.message, error);
  }
});


// FETCH Business Sales Records
exports.fetchSales = functions.https.onCall(async (_data, context) => {
  try {
    const privateKey = _data.key;
    const signer = new ethers.Wallet(privateKey, provider);
    const bookkeeperContract = new ethers.Contract(BookkeeperContractAddress, BookkeeperABI, signer);
    // fetching the business records using the from the Bookkeeper contracts
    const response = await bookkeeperContract.fetchSales();
    const records = [];
    await Promise.all(response.map(async (i) => {
      const item = {
        recordId: i.recordId.toNumber(),
        title: i.title,
        recordType: i.recordType,
        timestamp: i.timestamp,
        recordDate: i.recordDate.toNumber(),
        price: i.price.toNumber(),
        quantity: i.quantity.toNumber(),
        salePrice: i.salePrice.toNumber(),
        brandID: i.brandID.toString(),
      };
      records.push(item);
    }));

    return records;
  } catch (error) {
    console.error(error);
    // Re-throwing the error as an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError("unknown", error.message, error);
  }
});


// FETCH BUSINESS CREDIT SCORE
exports.fetchCreditScore = functions.https.onCall(async (data, context) => {
  try {
    const privateKey = data.key;
    const signer = new ethers.Wallet(privateKey, provider);
    const bookkeeperContract = new ethers.Contract(BookkeeperContractAddress, BookkeeperABI, signer);

    // fetching the business records using the from the Bookkeeper contracts
    const stockResponse = await bookkeeperContract.fetchStocks();
    const totalStock = [];
    await Promise.all(stockResponse.map(async (i) => {
      const item = {
        recordId: i.recordId.toNumber(),
        title: i.title,
        recordType: i.recordType,
        timestamp: i.timestamp,
        recordDate: i.recordDate.toNumber(),
        price: i.price.toNumber(),
        quantity: i.quantity.toNumber(),
        salePrice: i.salePrice.toNumber(),
        brandID: i.brandID.toString(),
      };
      totalStock.push(item);
    }));


    // fetching the business records using the from the Bookkeeper contracts
    const salesResponse = await bookkeeperContract.fetchSales();
    const totalSales = [];
    await Promise.all(salesResponse.map(async (i) => {
      const item = {
        recordId: i.recordId.toNumber(),
        title: i.title,
        recordType: i.recordType,
        timestamp: i.timestamp,
        recordDate: i.recordDate.toNumber(),
        price: i.price.toNumber(),
        quantity: i.quantity.toNumber(),
        salePrice: i.salePrice.toNumber(),
        brandID: i.brandID.toString(),
      };
      totalSales.push(item);
    }));

    // calculate sales to stock ratio
    const salesToStockRatio = totalSales / totalStock;

    // determine credit score based on sales to stock ratio
    let creditScore;

    if (totalSales.length === 0 || totalStock.length === 0) {
      creditScore = 0;
    } else {
      if (salesToStockRatio >= 2) {
        creditScore = 5;
      } else if (salesToStockRatio >= 1.5) {
        creditScore = 4;
      } else if (salesToStockRatio >= 1) {
        creditScore = 3;
      } else if (salesToStockRatio >= 0.5) {
        creditScore = 2;
      } else {
        creditScore = 1;
      }
    }


    // return credit score
    return {
      score: creditScore.toString(),
    };
  } catch (error) {
    console.error(error);
    // Re-throwing the error as an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError("unknown", error.message, error);
  }
});


exports.createStock = functions.https.onCall(async (data, context) => {
  try {
    const privateKey = data.key;
    const title = data.title;
    const recordType = data.recordType;
    const timestamp = data.timestamp;
    const price = data.price;
    const quantity = data.quantity;
    const salesPrice = data.salesPrice;
    const signer = new ethers.Wallet(privateKey, provider);
    const bookkeeperContract = new ethers.Contract(BookkeeperContractAddress, BookkeeperABI, signer);
    // Add New Business Stock
    const transaction = await bookkeeperContract.createStock(title, recordType, timestamp, price, quantity, salesPrice);
    const tx = await transaction.wait();
    const confirmationLink = `https://beryx.zondax.ch/v1/search/fil/hyperspace/transactions/${tx.transactionHash}`;
    console.log(tx);
    return {
      link: confirmationLink,
    };
  } catch (error) {
    console.error(error);
    // Re-throwing the error as an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError("unknown", error.message, error);
  }
});

exports.createSale = functions.https.onCall(async (data, context) => {
  try {
    const privateKey = data.key;
    const title = data.title;
    const recordType = data.recordType;
    const timestamp = data.timestamp;
    const price = data.price;
    const quantity = data.quantity;
    const salesPrice = data.salesPrice;
    const signer = new ethers.Wallet(privateKey, provider);
    const bookkeeperContract = new ethers.Contract(BookkeeperContractAddress, BookkeeperABI, signer);
    // Add New Business Sales Record
    const transaction = await bookkeeperContract.createSales(title, recordType, timestamp, price, quantity, salesPrice);
    const tx = await transaction.wait();
    const confirmationLink = `https://beryx.zondax.ch/v1/search/fil/hyperspace/transactions/${tx.transactionHash}`;
    console.log(tx);
    return {
      link: confirmationLink,
    };
  } catch (error) {
    console.error(error);
    // Re-throwing the error as an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError("unknown", error.message, error);
  }
});
