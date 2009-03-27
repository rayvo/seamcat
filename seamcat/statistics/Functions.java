// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Functions.java

package org.seamcat.statistics;


public final class Functions
{

    public Functions()
    {
    }

    public static double[] getProbabilities(double a[])
    {
        int n = a.length;
        double p[] = new double[n];
        double sum = 0.0D;
        for(int i = 0; i < n; i++)
            if(a[i] < 0.0D)
                a[i] = 0.0D;
            else
                sum += a[i];

        if(sum == 0.0D)
        {
            for(int i = 0; i < n; i++)
                p[i] = 1.0D / (double)n;

        } else
        {
            for(int i = 0; i < n; i++)
                p[i] = a[i] / sum;

        }
        return p;
    }

    public static double getProbability(double p)
    {
        if(p < 0.0D)
            return 0.0D;
        if(p > 1.0D)
            return 1.0D;
        else
            return p;
    }

    public static int getIndex(int i, int n)
    {
        if(i < 0)
            return 0;
        if(i > n - 1)
            return n - 1;
        else
            return i;
    }

    public static double perm(double n, int k)
    {
        if(((double)k > n) | (k < 0))
            return 0.0D;
        double prod = 1.0D;
        for(int i = 1; i <= k; i++)
            prod *= (n - (double)i) + 1.0D;

        return prod;
    }

    public static double factorial(int k)
    {
        return perm(k, k);
    }

    public static double comb(double n, int k)
    {
        return perm(n, k) / factorial(k);
    }

    public static double logGamma(double x)
    {
        double coef[] = {
            76.180091730000001D, -86.505320330000004D, 24.014098220000001D, -1.231739516D, 0.00120858003D, -5.3638199999999999E-006D
        };
        double step = 2.5066282746500002D;
        double fpf = 5.5D;
        double t = x - 1.0D;
        double tmp = t + fpf;
        tmp = (t + 0.5D) * Math.log(tmp) - tmp;
        double ser = 1.0D;
        for(int i = 1; i <= 6; i++)
        {
            t++;
            ser += coef[i - 1] / t;
        }

        return tmp + Math.log(step * ser);
    }

    public static double gamma(double x)
    {
        return Math.exp(logGamma(x));
    }

    public static double gammaCDF(double x, double a)
    {
        if(x <= 0.0D)
            return 0.0D;
        if(x < a + 1.0D)
            return gammaSeries(x, a);
        else
            return 1.0D - gammaCF(x, a);
    }

    private static double gammaSeries(double x, double a)
    {
        int maxit = 100;
        double eps = 2.9999999999999999E-007D;
        double sum = 1.0D / a;
        double ap = a;
        double gln = logGamma(a);
        double del = sum;
        int n = 1;
        do
        {
            if(n > maxit)
                break;
            ap++;
            del = (del * x) / ap;
            sum += del;
            if(Math.abs(del) < Math.abs(sum) * eps)
                break;
            n++;
        } while(true);
        return sum * Math.exp((-x + a * Math.log(x)) - gln);
    }

    private static double gammaCF(double x, double a)
    {
        int maxit = 100;
        double eps = 2.9999999999999999E-007D;
        double gln = logGamma(a);
        double g = 0.0D;
        double gOld = 0.0D;
        double a0 = 1.0D;
        double a1 = x;
        double b0 = 0.0D;
        double b1 = 1.0D;
        double fac = 1.0D;
        for(int n = 1; n <= maxit; n++)
        {
            double an = 1.0D * (double)n;
            double ana = an - a;
            a0 = (a1 + a0 * ana) * fac;
            b0 = (b1 + b0 * ana) * fac;
            double anf = an * fac;
            a1 = x * a0 + anf * a1;
            b1 = x * b0 + anf * b1;
            if(a1 == 0.0D)
                continue;
            fac = 1.0D / a1;
            g = b1 * fac;
            if(Math.abs((g - gOld) / g) < eps)
                break;
            gOld = g;
        }

        return Math.exp((-x + a * Math.log(x)) - gln) * g;
    }

    public static double betaCDF(double x, double a, double b)
    {
        double bt;
        if((x == 0.0D) | (x == 1.0D))
            bt = 0.0D;
        else
            bt = Math.exp((logGamma(a + b) - logGamma(a) - logGamma(b)) + a * Math.log(x) + b * Math.log(1.0D - x));
        if(x < (a + 1.0D) / (a + b + 2D))
            return (bt * betaCF(x, a, b)) / a;
        else
            return 1.0D - (bt * betaCF(1.0D - x, b, a)) / b;
    }

    private static double betaCF(double x, double a, double b)
    {
        int maxit = 100;
        double eps = 2.9999999999999999E-007D;
        double am = 1.0D;
        double bm = 1.0D;
        double az = 1.0D;
        double qab = a + b;
        double qap = a + 1.0D;
        double qam = a - 1.0D;
        double bz = 1.0D - (qab * x) / qap;
        int m = 1;
        do
        {
            if(m > maxit)
                break;
            double em = m;
            double tem = em + em;
            double d = (em * (b - (double)m) * x) / ((qam + tem) * (a + tem));
            double ap = az + d * am;
            double bp = bz + d * bm;
            d = (-(a + em) * (qab + em) * x) / ((a + tem) * (qap + tem));
            double app = ap + d * az;
            double bpp = bp + d * bz;
            double aOld = az;
            am = ap / bpp;
            bm = bp / bpp;
            az = app / bpp;
            bz = 1.0D;
            if(Math.abs(az - aOld) < eps * Math.abs(az))
                break;
            m++;
        } while(true);
        return az;
    }

    public static double beta(double a, double b)
    {
        return (gamma(a) * gamma(b)) / gamma(a + b);
    }

    public static int[] getSample(int p[], int n, int t)
    {
        int m = p.length;
        if(n < 1)
            n = 1;
        else
        if(n > m)
            n = m;
        int s[] = new int[n];
        if(t == 1)
        {
            for(int i = 0; i < n; i++)
            {
                int u = (int)((double)m * Math.random());
                s[i] = p[u];
            }

        } else
        {
            for(int i = 0; i < n; i++)
            {
                int k = m - i;
                int u = (int)((double)k * Math.random());
                s[i] = p[u];
                int temp = p[k - 1];
                p[k - 1] = p[u];
                p[u] = temp;
            }

        }
        return s;
    }

    public static int[] getSample(int m, int n, int t)
    {
        if(m < 1)
            m = 1;
        if(n < 1)
            n = 1;
        else
        if(n > m)
            n = m;
        int p[] = new int[m];
        for(int i = 0; i < m; i++)
            p[i] = i + 1;

        return getSample(p, n, t);
    }

    public static double[] sort(double a[])
    {
        int n = a.length;
        double b[] = new double[n];
        for(int i = 0; i < n; i++)
        {
            boolean smallest = true;
            int j = i - 1;
            do
            {
                if(j < 0)
                    break;
                if(b[j] <= a[i])
                {
                    b[j + 1] = a[i];
                    smallest = false;
                    break;
                }
                b[j + 1] = b[j];
                j--;
            } while(true);
            if(smallest)
                b[0] = a[i];
        }

        return b;
    }

    public static int[] sort(int a[])
    {
        int n = a.length;
        int b[] = new int[n];
        for(int i = 0; i < n; i++)
        {
            boolean smallest = true;
            int j = i - 1;
            do
            {
                if(j < 0)
                    break;
                if(b[j] <= a[i])
                {
                    b[j + 1] = a[i];
                    smallest = false;
                    break;
                }
                b[j + 1] = b[j];
                j--;
            } while(true);
            if(smallest)
                b[0] = a[i];
        }

        return b;
    }

    public static boolean isReal(double x)
    {
        return !(Double.isInfinite(x) | Double.isNaN(x));
    }

    public static final int WITHOUT_REPLACEMENT = 0;
    public static final int WITH_REPLACEMENT = 1;
}
