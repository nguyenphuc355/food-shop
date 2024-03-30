import { Component, OnInit } from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {IFood} from '../../model/i-food';
import {FoodService} from '../../service/food.service';
import {Title} from '@angular/platform-browser';
import {Router} from '@angular/router';
import {CartDto} from '../../dto/cart-dto';
import Swal from 'sweetalert2';
import {TokenStorageService} from '../../service/token-storage.service';

@Component({
  selector: 'app-meat',
  templateUrl: './meat.component.html',
  styleUrls: ['./meat.component.css']
})
export class MeatComponent implements OnInit {

  pageSize = 4;
  foodList$: Observable<IFood[]> | undefined;
  total$: Observable<number>;
  nameSearch = '';
  action: boolean;
  numberRecord = 0;
  content: boolean;
  totalRecord = 0;
  username:string;

  constructor(private foodService: FoodService,
              private title: Title,
              private tokenService: TokenStorageService,
              private router: Router) {
    this.title.setTitle('Trang chủ');
  }

  ngOnInit(): void {
    this.paginate(this.nameSearch, this.pageSize);
    this.getCustomer();
  }

  paginate(nameSearch, pageSize) {
    this.foodService.getAllMeat(nameSearch, pageSize).subscribe(data => {
      console.log(data);
      if (data != null) {
        this.action = true;
        this.foodList$ = new BehaviorSubject<IFood[]>(data.content);
        this.total$ = new BehaviorSubject<number>(data.totalElements);
      } else {
        this.action = false;
      }
    });
  }


  getCustomer(): void {
    this.username = this.tokenService.getUser().username;
  }

  nextPage() {
    this.pageSize += 4;
    this.paginate(this.nameSearch, this.pageSize);
  }

  resetSearchInput(): void {
    this.nameSearch = '';
  }

  addToCart(item: CartDto) {
    if (this.username == null) {
      Swal.fire({
        position: 'center',
        icon: 'warning',
        title: 'Bạn chưa đăng nhập, vui lòng đăng nhập trước !',
        showConfirmButton: false,
        timer: 2000
      });
      this.router.navigateByUrl('/login');
    } else {
      this.foodService.updateCart(item, this.username).subscribe(() => {
        const Toast = Swal.mixin({
          toast: true,
          position: 'top-end',
          timer: 3000,
          timerProgressBar: true
        });
        Toast.fire('Thêm Vào Giỏ Hàng Thành Công', '', 'success');
      });
    }
  }
}
