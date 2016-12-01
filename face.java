package data;


import java.util.ArrayList;

import data.point;

public class face{

	// list ot store the points
	ArrayList points;
	point centroid;

	//contructor to intialize the object
	public face(){	points =  new ArrayList();}

	//returns the number  of points
	public int ret_num(){return this.points.size()-1;}

	//returns the  point of current index..
	public point  ret_point(int index){return (point)this.points.get(index);}

	//
	public void add_point(point p)
	{
		if(!points.contains(p) || points.indexOf(p) == 0)
		{
			if(points.add(p));//{p.print_point();System.out.println("added");}
			else;//{p.print_point();System.out.println("not added");}
		}
		else System.out.println("point already loaded in face");

	}

	//
	void calc_centroid()
	{
		if(this.points.size()>2)
		{
			double x,y,z;
			x=(double)0;
			y=(double)0;
			z=(double)0;
			for(int i=0;i<this.points.size()-1;i++)
			{
				x = x + ((point)this.points.get(i)).ret_x();
				y = y + ((point)this.points.get(i)).ret_y();
				z = z + ((point)this.points.get(i)).ret_z();
			}

			x=x/((double)this.points.size()-1);
			y=y/((double)this.points.size()-1);
			z=z/((double)this.points.size()-1);
			this.centroid = new point(x,y,z);

		}
		else System.out.println(this.points.size()+ "  not enuf points in the face...");

	}

	public point ret_centroid(){ return this.centroid;}

	// check if point is in face...
	public boolean find_point(point p){return this.points.contains(p);}



	//
	public point get_face_point(point p)
	{

		if(this.points.contains(p))
		{
			double x,y,z;
			point temp1,temp2,temp3,temp4;
			int first = this.points.indexOf(p);
			int last  = this.points.lastIndexOf(p);

			face f = new face();
			//add current point
			temp1 = (point)this.points.get(first);
			f.add_point(temp1);

			x = (((point)this.points.get(first)).ret_x() + ((point)this.points.get(first+1)).ret_x())/(float)2;
			y = (((point)this.points.get(first)).ret_y() + ((point)this.points.get(first+1)).ret_y())/(float)2;
			z = (((point)this.points.get(first)).ret_z() + ((point)this.points.get(first+1)).ret_z())/(float)2;
			temp2 =  new point(x,y,z);
			//get center of line between next point
			f.add_point(temp2);

			temp3 = this.centroid;
			//add centroid of big face
			f.add_point(temp3);


			x = (((point)this.points.get(last)).ret_x() + ((point)this.points.get(last-1)).ret_x())/(float)2;
			y = (((point)this.points.get(last)).ret_y() + ((point)this.points.get(last-1)).ret_y())/(float)2;
			z = (((point)this.points.get(last)).ret_z() + ((point)this.points.get(last-1)).ret_z())/(float)2;
			temp4 =  new point(x,y,z);
			//get center of line between previous point
			f.add_point(temp4);

			//add current point again
			f.add_point(temp1);
			//calculate teh centroid of subface formed...
			f.calc_centroid();
			//return the centroid...
			return f.ret_centroid();

		}
		else
		{
			System.out.println("point not in face...");
			return null;
		}
	}



	// get new face from current face
	public face get_face()
	{
		calc_centroid();
		face f =  new face();
		for(int i = 0; i<this.points.size();i++)
		{
			point p =  this.get_face_point((point)points.get(i));
			f.add_point(p);
		}

		//f.calc_centroid();
		return f;
	}

	public boolean equal(face f)
	{
		boolean ret= false;
		int count=0;
		if(this.points.size() == f.ret_num())
		{
			for(int i=0;i<this.points.size();i++)
			{
				point p = (point)this.points.get(i);
				for(int j=0;j<f.ret_num();j++)
				{
					point q = (point)f.ret_point(j);
					if(p.equal(q))
					{
						count++;
						break;
					}
				}
			}

		}
		if(count == this.points.size())ret = true;
		return ret;
	}
	public void print_face()
	{
		for(int i=0;i<this.points.size();i++)
		{
			((point)points.get(i)).print_point();
		}
	}




	public static void main(String args[])
	{

		face f = new face();
		point p1 = new point((float)0,(float)0,(float)0);
		f.add_point(p1);
		point p2 = new point((float)4,(float)0,(float)0);
		f.add_point(p2);
		point p3 = new point((float)4,(float)4,(float)0);
		f.add_point(p3);
		point p4 = new point((float)0,(float)4,(float)0);
		f.add_point(p4);
		f.add_point(p1);
		face f1 = f.get_face();
		f1.print_face();
		System.out.println(f1.ret_num());


	}


}
