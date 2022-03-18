/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card;

import eu.europeanstudentcard.esc.EscnFactory;
import eu.europeanstudentcard.esc.EscnFactoryException;


class Work {

    public static void main(final String[] args) {

        try {
            System.out.println(EscnFactory.getEscn(0, "999893752"));
        } catch ( EscnFactoryException|InterruptedException e ) {
            e.printStackTrace();
        }
    }

}
