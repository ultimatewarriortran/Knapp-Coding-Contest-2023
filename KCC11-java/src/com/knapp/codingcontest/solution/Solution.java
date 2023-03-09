/* -*- java -*-
# =========================================================================== #
#                                                                             #
#                         Copyright (C) KNAPP AG                              #
#                                                                             #
#       The copyright to the computer program(s) herein is the property       #
#       of Knapp.  The program(s) may be used   and/or copied only with       #
#       the  written permission of  Knapp  or in  accordance  with  the       #
#       terms and conditions stipulated in the agreement/contract under       #
#       which the program(s) have been supplied.                              #
#                                                                             #
# =========================================================================== #
*/

package com.knapp.codingcontest.solution;

import java.util.*;

import com.knapp.codingcontest.data.*;
import com.knapp.codingcontest.operations.CostFactors;
import com.knapp.codingcontest.operations.InfoSnapshot;
import com.knapp.codingcontest.operations.Operations;
import com.knapp.codingcontest.operations.ex.NoStockInWarehouseException;
import com.knapp.codingcontest.operations.ex.OrderLineAlreadyPackedException;

/**
 * This is the code YOU have to provide
 *
 * @param warehouse all the operations you should need
 */
public class Solution {
  public String getParticipantName() {

    return "Luther Tran"; // TODO: return your name
  }

  public Institute getParticipantInstitution() {
    return Institute.HTL_Rennweg_Wien; // TODO: return the Id of your institute - please refer to the hand-out
  }

  // ----------------------------------------------------------------------------

  protected final InputData input;
  protected final Operations operations;

  // ----------------------------------------------------------------------------

  public Solution(final InputData input, final Operations operations) throws NoStockInWarehouseException, OrderLineAlreadyPackedException {
    this.input = input;
    this.operations = operations;

    for (int k = 0; k < 10000; k++) {
      double min = 490;
      for (int i = 0; i < 20; i++) {
        Warehouse warehouse = input.getWarehouses().get(i);
        Position warehousePos = warehouse.getPosition();
        OrderLine orderLine = input.getOrderLines().get(k);
        Customer customer = input.getOrderLines().get(k).getCustomer();
        Position customerPos = customer.getPosition();
        double distance = customerPos.calculateDistance(warehousePos);
        Product product = input.getOrderLines().get(k).getProduct();
        for (int j = 0; j < 20; j++) {
          if (distance < min) {
            if (warehouse.hasStock(product))
              try {
                operations.ship(orderLine, warehouse);
              } catch (OrderLineAlreadyPackedException | NoStockInWarehouseException ignored) {
              }
            /**min = distance;
             System.out.println(min +" + "+ warehouse.getCode());**/
          }
        }
      }
    }
  }





  // ----------------------------------------------------------------------------

  /**
   * The main entry-point.
   *
   * calculation for shipments costs:
   *    total_cost = sum{products per warehouse/customer} ((cost_base + (sum(size_products) * cost_size)) * distanz_warehouse_to_customer)
   *
   * some hints:
   *   - one shipments is: all products for one customer from one warehouse (will be handled and calculated automatically/internally)
   *   - there are finite amounts of product stocks in the warehouses (stock will be adjusted automatically by using op.ship() method)
   *   - not all warehouses have all products on stock - or stock might run out (may be checked via wh.hasStock())
   *
   * optimization is possible along two factors:
   *   - minimize warehouse/customer pairs (#shipments) - reduce cost_base impact on total costs
   *   - minimize distances - shipments from closer warehouses are cheaper
   *
   * some ideas for finding a better solution:
   *   sometimes it might be beneficial to split an order to have most delivered from close warehouse and only some from farther
   *   instead of trying to deliver everything from just one warehouse that is far away
   */
  public void run() throws Exception {
    // TODO: make calls to API (see below)
    // lines containing '@TODO' are removed before packaging contest-sandbox
  }

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  /**
   * Just for documentation purposes.
   *
   * Method may be removed without any side-effects
   *
   *   divided into 4 sections
   *
   *     <li><em>input methods</em>
   *
   *     <li><em>main interaction methods</em>
   *         - these methods are the ones that make (explicit) changes to the warehouse
   *
   *     <li><em>information</em>
   *         - information you might need for your solution
   *
   *     <li><em>additional information</em>
   *         - various other infos: statistics, information about (current) costs, ...
   *
   */
  @SuppressWarnings("unused")
  private void apis() throws Exception {
    // ----- input -----

    final List<OrderLine> orderLines = input.getOrderLines();
    final List<Warehouse> warehouses = input.getWarehouses();

    final OrderLine orderLine = orderLines.get(0);
    final Warehouse warehouse = warehouses.get(0);

    // ----- main interaction methods -----

    operations.ship(orderLine, warehouse); // throws OrderLineAlreadyPackedException, NoStockInWarehouseException;

    // ----- information -----

    final boolean hasStock = warehouse.hasStock(orderLine.getProduct());
    final Map<Product, Integer> currentStocks = warehouse.getCurrentStocks();

    final double distance = orderLine.getCustomer().getPosition().calculateDistance(warehouse.getPosition());

    // ----- additional information -----

    final CostFactors costFactors = operations.getCostFactors();

    final InfoSnapshot info = operations.getInfoSnapshot();
    final int unfinishedOrderLineCount = info.getUnfinishedOrderLineCount();
    final double unfinishedOrderLinesCost = info.getUnfinishedOrderLinesCost();
    final double shipmentsCost = info.getShipmentsCost();
    final double totalCost = info.getTotalCost();
  }

  // ----------------------------------------------------------------------------
}
