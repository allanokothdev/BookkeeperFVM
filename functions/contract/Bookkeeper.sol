// SPDX-License-Identifier: UNLICENSED
pragma solidity 0.8.9;

import "hardhat/console.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";

contract Bookkeeper is ReentrancyGuard {

    using Counters for Counters.Counter;
    Counters.Counter private salesIds;
    Counters.Counter private stockIds;
    mapping (uint256 => Record) public salesList;
    mapping (uint256 => Record) public stockList;

    struct Record {
        uint256 recordId;
        string title;
        string recordType;
        string timestamp;
        uint256 recordDate;
        uint256 price;
        uint256 quantity;
        uint256 salesPrice;
        address brandID;
    }

    event RecordItemCreated (
       uint256 recordId,
        string title,
        string recordType,
        string timestamp,
        uint256 recordDate,
        uint256 price,
        uint256 salesPrice,
        uint256 quantity,
        address brandID
    );

    function createStock(string memory _title, string memory _recordType, string memory _timestamp, uint256 _price, uint256 _quantity, uint256 _salesPrice)
    public nonReentrant {
        stockIds.increment();
        uint256 newRecordId = stockIds.current();
        stockList[newRecordId] = Record(newRecordId, _title, _recordType, _timestamp, block.timestamp, _price, _quantity, _salesPrice, msg.sender);
        emit RecordItemCreated(newRecordId, _title, _recordType, _timestamp, block.timestamp, _price, _quantity, _salesPrice, msg.sender);
    }


    function createSales(string memory _title, string memory _recordType, string memory _timestamp, uint256 _price, uint256 _quantity, uint256 _salesPrice)
    public nonReentrant {
        salesIds.increment();
        uint256 newRecordId = salesIds.current();
        salesList[newRecordId] = Record(newRecordId, _title, _recordType, _timestamp, block.timestamp, _price, _quantity, _salesPrice, msg.sender);
        emit RecordItemCreated(newRecordId, _title, _recordType, _timestamp, block.timestamp, _price, _quantity, _salesPrice, msg.sender);
    }


    /* Returns only stock uploaded by a respective business */
    function fetchStocks() public view returns (Record[] memory) {
      uint totalItemCount = stockIds.current();
      uint itemCount = 0;
      uint currentIndex = 0;

      for (uint i = 0; i < totalItemCount; i++) {
        if (stockList[i + 1].brandID == msg.sender) {
          itemCount += 1;
        }
      }

      Record[] memory items = new Record[](itemCount);
      for (uint i = 0; i < totalItemCount; i++) {
        if (stockList[i + 1].brandID == msg.sender) {
          uint currentId = i + 1;
          Record storage currentItem = stockList[currentId];
          items[currentIndex] = currentItem;
          currentIndex += 1;
        }
      }
      return items;
    }


    /* Returns only stock uploaded by a respective business */
    function fetchSales() public view returns (Record[] memory) {
      uint totalItemCount = salesIds.current();
      uint itemCount = 0;
      uint currentIndex = 0;

      for (uint i = 0; i < totalItemCount; i++) {
        if (salesList[i + 1].brandID == msg.sender) {
          itemCount += 1;
        }
      }

      Record[] memory items = new Record[](itemCount);
      for (uint i = 0; i < totalItemCount; i++) {
        if (salesList[i + 1].brandID == msg.sender) {
          uint currentId = i + 1;
          Record storage currentItem = salesList[currentId];
          items[currentIndex] = currentItem;
          currentIndex += 1;
        }
      }
      return items;
    }
}


