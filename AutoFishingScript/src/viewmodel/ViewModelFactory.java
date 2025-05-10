package viewmodel;

import model.Model;

public class ViewModelFactory
{

private AutoFishingViewModel autoFishingViewModel;

  public ViewModelFactory(Model model){

    this.autoFishingViewModel=new AutoFishingViewModel(model);
  }


  public AutoFishingViewModel getAutoFishingViewModel()
  {
    return autoFishingViewModel;
  }

}
