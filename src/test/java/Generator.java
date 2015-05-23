/*
    Copyright 2015 Mahesh Khanwalkar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Generator
{
    public static void main(String[] args) throws IOException
    {
        String[] states = new String[]
                {
                        "AL", "AK", "AZ", "AR", "CA", "CO",
                        "CT", "DE", "FL", "GA", "HI", "ID",
                        "IL", "IN", "IA", "KS", "KY", "LA",
                        "ME", "MD", "MA", "MI", "MN", "MS",
                        "MO", "MT", "NE", "NV", "NH", "NJ",
                        "NM", "NY", "NC", "ND", "OH", "OK",
                        "OR", "PA", "RI", "SC", "SD", "TN",
                        "TX", "UT", "VT", "VA", "WA", "WV",
                        "WI", "WY"
                };


        int BUF_SIZE = 1000;
        Random r = new Random();

        BufferedWriter bw = new BufferedWriter(new FileWriter("data.txt"));

        for(int i = 0; i < BUF_SIZE; i++)
        {
            String state = states[r.nextInt(states.length)];
            String age = Integer.toString(r.nextInt(100) + 1);

            String income = Integer.toString(r.nextInt(1000000) + 50000);
            bw.write(state + "," + age + "," + income + "\n");
        }

        bw.flush();
        bw.close();
    }
}
