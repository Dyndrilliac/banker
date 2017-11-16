import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import Jama.Matrix;

public class BankersAlgorithm
{
    protected static class RequestData
    {
        private int        process;
        private double[][] request;

        public RequestData(final int process, final double[][] request)
        {
            super();
            this.setProcess(process);
            this.setRequest(request);
        }

        public final int getProcess()
        {
            return this.process;
        }

        public final double[][] getRequest()
        {
            return this.request;
        }

        protected final void setProcess(final int process)
        {
            this.process = process;
        }

        protected final void setRequest(final double[][] request)
        {
            this.request = request;
        }
    }

    public static BankersAlgorithm inputInitialData(final Scanner input) throws InputMismatchException, NoSuchElementException
    {
        int n = 0, m = 0;
        double[][] available = null, allocation = null, max = null;

        System.out.println("Please enter the number of processes.");
        n = input.nextInt();

        System.out.println("\nPlease enter the number of resource types.");
        m = input.nextInt();

        System.out.println("\nPlease enter the available vector.");
        available = new double[1][m];

        for ( int j = 0; j < m; j++ )
        {
            available[0][j] = input.nextDouble();
        }

        System.out.println("\nPlease enter the max matrix.");
        max = new double[n][m];

        for ( int i = 0; i < n; i++ )
        {
            for ( int j = 0; j < m; j++ )
            {
                max[i][j] = input.nextDouble();
            }
        }

        System.out.println("\nPlease enter the allocation matrix.");
        allocation = new double[n][m];

        for ( int i = 0; i < n; i++ )
        {
            for ( int j = 0; j < m; j++ )
            {
                allocation[i][j] = input.nextDouble();
            }
        }

        System.out.println();
        return new BankersAlgorithm(n, m, new Matrix(available), new Matrix(max), new Matrix(allocation));
    }

    public static RequestData inputRequestData(final Scanner input, final int m) throws InputMismatchException, NoSuchElementException
    {
        int process = 0;
        double[][] request = null;

        System.out.println("\nWhich process is requesting increased resources?");
        process = input.nextInt();

        System.out.println("\nPlease enter the request vector.");
        request = new double[1][m];

        for ( int j = 0; j < m; j++ )
        {
            request[0][j] = input.nextDouble();
        }

        System.out.println();
        return new RequestData(process, request);
    }

    public static void main(final String[] args)
    {
        Scanner input = new Scanner(System.in);

        try
        {
            BankersAlgorithm ba = BankersAlgorithm.inputInitialData(input);

            if ( ba.isSafe() )
            {
                System.out.println("Safe State!");

                RequestData rd = BankersAlgorithm.inputRequestData(input, ba.getM());

                if ( ba.request(rd.getProcess(), new Matrix(rd.getRequest())) )
                {
                    System.out.println("Request granted: Safe State!");
                }
                else
                {
                    System.out.println("Request denied: Unsafe State!");
                }
            }
            else
            {
                System.out.println("Unsafe State!");
            }
        }
        catch ( final Exception e )
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            input.close();
        }
    }

    /*
     * Let n be the number of processes.
     * Let m be the number of resource types.
     * 
     * Let available be a vector of length m.
     * If available[j] = k, then there are k instances of resource type R_j available.
     * 
     * Let max be a matrix of dimensions n x m.
     * If max[i,j] = k, then process P_i may request at most k instances of resource type R_j.
     * 
     * Let allocation be a matrix of dimensions n x m.
     * If allocation[i,j] = k, then process P_i is currently allocated k instances of R_j.
     * 
     * Let need be a matrix of dimensions n x m.
     * If need[i,j] = k, then process P_i may need k more instances of R_j to complete its task.
     * 
     * Note that need[i,j] = max[i,j] – allocation[i,j].
     */

    private Matrix allocation = null;
    private Matrix available  = null;
    private int    m          = 0;
    private Matrix max        = null;
    private int    n          = 0;

    public BankersAlgorithm(final int n, final int m, final Matrix available, final Matrix max, final Matrix allocation)
    {
        super();
        this.setN(n);
        this.setM(m);
        this.setAvailable(available);
        this.setMax(max);
        this.setAllocation(allocation);
    }

    protected final boolean check(final int i1, final int i2, final Matrix A, final Matrix B)
    {
        for ( int j = 0; j < this.getM(); j++ )
        {
            if ( A.getArray()[i1][j] < B.getArray()[i2][j] ) { return false; }
        }

        return true;
    }

    public final BankersAlgorithm copy()
    {
        // Creates a deep copy of this BankersAlgorithm object.
        return new BankersAlgorithm(this.getN(), this.getM(), this.getAvailable().copy(), this.getMax().copy(), this.getAllocation().copy());
    }

    public final Matrix getAllocation()
    {
        return this.allocation;
    }

    public final Matrix getAvailable()
    {
        return this.available;
    }

    public final int getM()
    {
        return this.m;
    }

    public final Matrix getMax()
    {
        return this.max;
    }

    public final int getN()
    {
        return this.n;
    }

    public final Matrix getNeed()
    {
        return this.getMax().minus(this.getAllocation());
    }

    public boolean isSafe()
    {
        /*
         * 1. Let work and finish be vectors of length m and n, respectively.
         *    work = available.
         *    finish[i] = false for i from 0 to n-1.
         * 
         * 2. Find a value for i such that both of the following conditions are true:
         *    (a) finish[i] = false.
         *    (b) need_i <= work.
         *    If no such i exists, then goto step 4.
         * 
         * 3. work = work + allocation_i
         *    finish[i] = true.
         *    Goto step 2.
         * 
         * 4. If finish[i] == true for i from 0 to n-1, then the system is in a safe state.
         */

        Matrix work = this.getAvailable().copy();
        boolean retVal = false, finish[] = new boolean[this.getN()];
        int k = 0;

        while ( k < this.getN() ) // Loop until all process have been allocated...
        {
            boolean allocated = false;

            for ( int i = 0; i < this.getN(); i++ )
            {
                // Checking to see if all needed resources for the ith process can be allocated.
                if ( ( !finish[i] ) && ( this.check(0, i, work, this.getNeed()) ) )
                {
                    // Trying to allocate...
                    for ( int j = 0; j < this.getM(); j++ )
                    {
                        work.getArray()[0][j] = work.getArray()[0][j] - this.getNeed().getArray()[i][j] + this.getMax().getArray()[i][j];
                    }

                    System.out.println("Allocated process: " + i);
                    System.out.println("Available resources: ");
                    work.print(2, 0);
                    allocated = finish[i] = true;
                    k++;
                }
            }

            if ( !allocated ) // If no allocation, then exit the loop.
            {
                break;
            }
        }

        if ( k == this.getN() ) // If all processes have been successfully allocated, then we have reached a safe state.
        {
            retVal = true;
        }

        return retVal;
    }

    public boolean request(final int process, final Matrix request)
    {
        /*
         * 1. First, check to make sure that the request is less than or equal to need_i.
         *    If it is, continue. If not, return failure.
         * 
         * 2. Next, check to make sure that the request is less than or equal to the available resources.
         *    If it is, continue. If not, return failure.
         * 
         * 3. Pretend to allocate resources to P_i by modifying the state as follows:
         *    available = available - request.
         *    allocation_i = allocation_i + request.
         *    need_i = need_i - request.
         * 
         * If this results in a safe state, then the resources are allocated to P_i.
         * If this results in an unsafe state, then P_i must wait and the previous state is restored.
         */

        boolean retVal = false;

        if ( this.check(process, 0, this.getNeed(), request) )
        {
            if ( this.check(0, 0, this.getAvailable(), request) )
            {
                // Make a temporary deep copy of this BankersAlgorithm object for modifications so the current stable state is not lost in the event of an error.
                BankersAlgorithm temp = this.copy();

                temp.getAvailable().minusEquals(request);

                for ( int j = 0; j < this.getM(); j++ )
                {
                    temp.getAllocation().getArray()[process][j] += request.getArray()[0][j];
                    temp.getNeed().getArray()[process][j] -= request.getArray()[0][j];
                }

                // Safety check.
                if ( temp.isSafe() )
                {
                    retVal = true;
                    this.setAvailable(temp.getAvailable());
                    this.setMax(temp.getMax());
                    this.setAllocation(temp.getAllocation());
                }
            }
        }

        return retVal;
    }

    protected final void setAllocation(final Matrix allocation)
    {
        this.allocation = allocation;
    }

    protected final void setAvailable(final Matrix available)
    {
        this.available = available;
    }

    protected final void setM(final int m)
    {
        this.m = m;
    }

    protected final void setMax(final Matrix max)
    {
        this.max = max;
    }

    protected final void setN(final int n)
    {
        this.n = n;
    }
}
